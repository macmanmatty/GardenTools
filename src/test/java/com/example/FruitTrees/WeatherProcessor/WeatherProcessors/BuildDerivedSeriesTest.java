package com.example.FruitTrees.WeatherProcessor.WeatherProcessors;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculator;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BuildDerivedSeriesTest {

    // ---- Minimal fake request/response types for canonicalUnit testing ----

    static class FakeWeatherRequest {
        private final Set<String> hourlyDataTypes = new LinkedHashSet<>();
        private final String temperatureUnit;

        FakeWeatherRequest(String temperatureUnit, String... needed) {
            this.temperatureUnit = temperatureUnit;
            this.hourlyDataTypes.addAll(Arrays.asList(needed));
        }

        public Set<String> getHourlyDataTypes() {
            return hourlyDataTypes;
        }

        public String getTemperatureUnit() {
            return temperatureUnit;
        }
    }

    static class FakeLocationResponse {
        private final Map<String, double[]> data = new HashMap<>();
        public Map<String, double[]> getData() { return data; }
    }

    /**
     * Fake factory that returns calculators by name.
     * It throws when the calculator name is unknown (to mimic Spring's "no such bean").
     */
    static class FakeWeatherProcessorFactory {
        private final Map<String, DerivedSeriesCalculator> calcs = new HashMap<>();

        public void register(DerivedSeriesCalculator calc) {
            calcs.put(calc.outputType(), calc);
        }

        public DerivedSeriesCalculator createDerivedSeriesCalculator(String name) {
            DerivedSeriesCalculator c = calcs.get(name);
            if (c == null) throw new NoSuchElementException("No such calculator: " + name);
            return c;
        }
    }

    // ---- A tiny service wrapper so we can call buildDerivedSeries() as-is ----

    static class TestableWeatherProcessorService {

        private final FakeWeatherProcessorFactory weatherProcessorFactory;

        TestableWeatherProcessorService(FakeWeatherProcessorFactory factory) {
            this.weatherProcessorFactory = factory;
        }

        public Map<String, double[]> buildDerivedSeries(FakeWeatherRequest weatherRequest,
                                                        FakeLocationResponse locationResponse) {

            Set<String> needed = new LinkedHashSet<>(weatherRequest.getHourlyDataTypes());
            Map<String, double[]> seriesByType = locationResponse.getData();

            if (weatherRequest.getTemperatureUnit().equalsIgnoreCase("fahrenheit")) {
                needed.add("temperature_2m_c");
                needed.add("dewpoint_2m_c");
            } else {
                needed.add("temperature_2m_f");
                needed.add("dewpoint_2m_f");
            }

            boolean progress;
            int safety = 0;

            do {
                progress = false;
                safety++;
                if (safety > 1000) {
                    throw new IllegalStateException("Derived series computation appears to be stuck in a loop");
                }

                for (String derivedType : new ArrayList<>(needed)) {
                    if (seriesByType.containsKey(derivedType)) continue;

                    DerivedSeriesCalculator calc;
                    try {
                        calc = weatherProcessorFactory.createDerivedSeriesCalculator(derivedType);
                    } catch (Exception e) {
                        continue; // not a derived type
                    }

                    List<String> reqTypes = calc.requiredInputTypes();
                    double[][] reqSeries = new double[reqTypes.size()][];
                    int minLen = Integer.MAX_VALUE;

                    boolean ok = true;
                    for (int i = 0; i < reqTypes.size(); i++) {
                        double[] s = seriesByType.get(reqTypes.get(i));
                        if (s == null) { ok = false; break; }
                        reqSeries[i] = s;
                        minLen = Math.min(minLen, s.length);
                    }
                    if (!ok || minLen == Integer.MAX_VALUE) continue;

                    List<String> optTypes = calc.optionalInputTypes();
                    List<double[]> optSeriesList = new ArrayList<>();
                    for (String ot : optTypes) {
                        double[] s = seriesByType.get(ot);
                        if (s != null) {
                            optSeriesList.add(s);
                        }
                    }
                    double[][] optSeries = optSeriesList.toArray(new double[0][]);

                    double[] out = new double[minLen];
                    double[] reqBuf = new double[reqSeries.length];
                    double[] optBuf = new double[optSeries.length];

                    for (int t = 0; t < minLen; t++) {
                        for (int i = 0; i < reqSeries.length; i++) reqBuf[i] = reqSeries[i][t];
                        for (int i = 0; i < optSeries.length; i++) {
                            double[] os = optSeries[i];
                            optBuf[i] = (t < os.length) ? os[t] : Double.NaN;
                        }
                        out[t] = calc.computeAt(reqBuf, optBuf);
                    }

                    seriesByType.put(derivedType, out);
                    progress = true;
                }

            } while (progress);

            return seriesByType;
        }
    }

    // ---- Fake calculators used by tests ----

    /** temperature_2m_c = temperature_2m_f converted (simple affine conversion) */
    static class TempFtoCCalc implements DerivedSeriesCalculator {
        @Override public String outputType() { return "temperature_2m_c"; }
        @Override public List<String> requiredInputTypes() { return List.of("temperature_2m_f"); }
        @Override public double computeAt(double[] req, double[] opt) {
            return (req[0] - 32.0) * 5.0 / 9.0;
        }
    }

    /** dewpoint_2m_c = dewpoint_2m_f converted */
    static class DewFtoCCalc implements DerivedSeriesCalculator {
        @Override public String outputType() { return "dewpoint_2m_c"; }
        @Override public List<String> requiredInputTypes() { return List.of("dewpoint_2m_f"); }
        @Override public double computeAt(double[] req, double[] opt) {
            return (req[0] - 32.0) * 5.0 / 9.0;
        }
    }

    /** vpd_kpa = temperature_2m_c - dewpoint_2m_c (not real physics; just easy to assert) */
    static class VpdDependsOnCelsiusCalc implements DerivedSeriesCalculator {
        @Override public String outputType() { return "vpd_kpa"; }
        @Override public List<String> requiredInputTypes() { return List.of("temperature_2m_c", "dewpoint_2m_c"); }
        @Override public double computeAt(double[] req, double[] opt) {
            return req[0] - req[1];
        }
    }

    /** uses optional series, but optional is shorter; should not clamp output length */
    static class UsesOptionalButShouldNotClampCalc implements DerivedSeriesCalculator {
        @Override public String outputType() { return "uses_opt"; }
        @Override public List<String> requiredInputTypes() { return List.of("a"); }
        @Override public List<String> optionalInputTypes() { return List.of("b"); }

        @Override public double computeAt(double[] req, double[] opt) {
            // if optional missing/NaN, add 0; otherwise add b
            double b = (opt.length > 0 && !Double.isNaN(opt[0])) ? opt[0] : 0.0;
            return req[0] + b;
        }
    }

    // -------------------- TESTS --------------------

    @Test
    void multiPass_computesDerivedThatDependsOnOtherDerived() {
        FakeWeatherProcessorFactory factory = new FakeWeatherProcessorFactory();
        factory.register(new TempFtoCCalc());
        factory.register(new DewFtoCCalc());
        factory.register(new VpdDependsOnCelsiusCalc());

        TestableWeatherProcessorService service = new TestableWeatherProcessorService(factory);

        FakeWeatherRequest req = new FakeWeatherRequest("fahrenheit", "vpd_kpa");
        FakeLocationResponse loc = new FakeLocationResponse();

        // Raw Fahrenheit series only
        loc.getData().put("temperature_2m_f", new double[]{32.0, 50.0}); // 0C, 10C
        loc.getData().put("dewpoint_2m_f",    new double[]{32.0, 41.0}); // 0C, 5C

        Map<String, double[]> out = service.buildDerivedSeries(req, loc);

        assertTrue(out.containsKey("temperature_2m_c"));
        assertTrue(out.containsKey("dewpoint_2m_c"));
        assertTrue(out.containsKey("vpd_kpa"));

        assertArrayEquals(new double[]{0.0, 10.0}, out.get("temperature_2m_c"), 1e-9);
        assertArrayEquals(new double[]{0.0, 5.0},  out.get("dewpoint_2m_c"), 1e-9);
        // vpd_kpa = tempC - dewC (per our fake calc) => [0-0, 10-5] = [0,5]
        assertArrayEquals(new double[]{0.0, 5.0},  out.get("vpd_kpa"), 1e-9);
    }

    @Test
    void optionalInputs_doNotClampOutputLength_andMissingOptionalBecomesNaN() {
        FakeWeatherProcessorFactory factory = new FakeWeatherProcessorFactory();
        factory.register(new UsesOptionalButShouldNotClampCalc());

        TestableWeatherProcessorService service = new TestableWeatherProcessorService(factory);

        FakeWeatherRequest req = new FakeWeatherRequest("celsius", "uses_opt");
        FakeLocationResponse loc = new FakeLocationResponse();

        // required is length 4; optional is length 2
        loc.getData().put("a", new double[]{1, 2, 3, 4});
        loc.getData().put("b", new double[]{10, 20});

        Map<String, double[]> out = service.buildDerivedSeries(req, loc);

        assertTrue(out.containsKey("uses_opt"));
        assertEquals(4, out.get("uses_opt").length);

        // computeAt adds b when present; otherwise b treated as 0
        assertArrayEquals(new double[]{11, 22, 3, 4}, out.get("uses_opt"), 1e-9);
    }

    @Test
    void requestUnitAddsCorrectCanonicalTargets_fahrenheitAddsCelsiusKeys() {
        FakeWeatherProcessorFactory factory = new FakeWeatherProcessorFactory();
        factory.register(new TempFtoCCalc());
        factory.register(new DewFtoCCalc());

        TestableWeatherProcessorService service = new TestableWeatherProcessorService(factory);

        FakeWeatherRequest req = new FakeWeatherRequest("fahrenheit"); // nothing explicitly requested
        FakeLocationResponse loc = new FakeLocationResponse();

        loc.getData().put("temperature_2m_f", new double[]{32.0});
        loc.getData().put("dewpoint_2m_f", new double[]{32.0});

        Map<String, double[]> out = service.buildDerivedSeries(req, loc);

        assertTrue(out.containsKey("temperature_2m_c"));
        assertTrue(out.containsKey("dewpoint_2m_c"));
    }
}
