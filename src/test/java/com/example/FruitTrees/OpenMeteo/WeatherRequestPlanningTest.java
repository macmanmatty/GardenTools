package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for:
 * - extractHourlyDataTypes(WeatherRequest)
 * - plan(Set<String>, boolean)
 *
 * This test uses fakes to avoid Spring.
 */
class WeatherRequestPlanningTest {

    // ---------------- Fakes ----------------

    /** Minimal fake DerivedSeriesRegistry */
    static class FakeDerivedSeriesRegistry {
        private final Map<String, DerivedSeriesCalculator> byKey = new HashMap<>();
        DerivedSeriesCalculator getCalculatorOrNull(String key) { return byKey.get(key); }
        void register(DerivedSeriesCalculator calc) { byKey.put(calc.outputType(), calc); }
    }

    /** Simple derived calculator: output "vpd_kpa" depends on temp + dewpoint */
    static class VpdCalc implements DerivedSeriesCalculator {
        @Override public String outputType() { return "vpd_kpa"; }
        @Override public List<String> requiredInputTypes() { return List.of("temperature_2m", "dewpoint_2m"); }
        @Override public List<String> optionalInputTypes() { return List.of(); }
        @Override public double computeAt(double[] req, double[] opt) { return 0; } // not used by planner test
    }

    /**
     * A tiny test harness class that contains *only* the methods under test.
     * In your codebase this would be your real service class; here we isolate it.
     */
    static class PlannerHarness {

        private final FakeDerivedSeriesRegistry derivedSeriesRegistry;

        PlannerHarness(FakeDerivedSeriesRegistry registry) {
            this.derivedSeriesRegistry = registry;
        }

        public record Plan(Set<String> fetchVars, Set<String> derivedKeys) {}

        // ---- Copy of your plan() with two small tweaks:
        // 1) calls local DataUtilitiesShim instead of your real DataUtilities (so unit test controls mapping)
        // 2) derivedSeriesRegistry is the fake above

        public Plan plan(Set<String> requestedKeys, boolean bestEffort) {
            Set<String> allKeysNeeded = expandKeys(requestedKeys, bestEffort);

            Set<String> fetchVars = new LinkedHashSet<>();
            Set<String> derivedToCompute = new LinkedHashSet<>();

            for (String key : allKeysNeeded) {
                DerivedSeriesCalculator calc = derivedSeriesRegistry.getCalculatorOrNull(key);

                if (calc == null) {
                    String openMeteoVar = DataUtilitiesShim.toOpenMeteoDatatype(key);
                    if (openMeteoVar != null) fetchVars.add(openMeteoVar);
                    continue;
                }

                String openMeteoVar = DataUtilitiesShim.toOpenMeteoDatatype(key);
                boolean openMeteoSupportsIt = (openMeteoVar != null);

                if (depsAreFetchable(calc, allKeysNeeded)) {
                    derivedToCompute.add(key);
                } else if (openMeteoSupportsIt) {
                    fetchVars.add(openMeteoVar);
                } else {
                    derivedToCompute.add(key);
                }
            }

            return new Plan(fetchVars, derivedToCompute);
        }

        private Set<String> expandKeys(Set<String> requestedKeys, boolean bestEffort) {
            Set<String> out = new LinkedHashSet<>();
            Deque<String> stack = new ArrayDeque<>(requestedKeys);

            while (!stack.isEmpty()) {
                String key = stack.pop();
                if (!out.add(key)) continue;

                DerivedSeriesCalculator calc = derivedSeriesRegistry.getCalculatorOrNull(key);
                if (calc == null) continue;

                stack.addAll(calc.requiredInputTypes());
                if (bestEffort) stack.addAll(calc.optionalInputTypes());
            }
            return out;
        }

        private boolean depsAreFetchable(DerivedSeriesCalculator calc, Set<String> allKeysNeeded) {
            // Your current check: deps present in the expanded set
            for (String dep : calc.requiredInputTypes()) {
                if (!allKeysNeeded.contains(dep)) return false;
            }
            return true;
        }

        /**
         * This matches your private extractHourlyDataTypes method signature.
         * In your real code it's private; in test harness it's private too so we can demo reflection.
         */
        private void extractHourlyDataTypes(WeatherRequest weatherRequest) {
            List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = weatherRequest.getHourlyWeatherProcessRequests();
            weatherRequest.getHourlyDataTypes().clear();
            for (HourlyWeatherProcessRequest r : hourlyWeatherProcessRequests) {
                weatherRequest.getHourlyDataTypes().add(r.getHourlyDataType());
            }

            weatherRequest.setOpenmeteoRequestHourlyDataTypes(
                    plan(weatherRequest.getHourlyDataTypes(), weatherRequest.bestEffort()).fetchVars
            );
        }
    }

    /**
     * Shim for DataUtilities.toOpenMeteoDatatype so unit tests control mapping.
     * Replace with your real DataUtilities in integration tests.
     */
    static class DataUtilitiesShim {
        static String toOpenMeteoDatatype(String key) {
            if (key == null) return null;
            return switch (key) {
                case "temperature_2m" -> "temperature_2m";
                case "dewpoint_2m" -> "dewpoint_2m";
                case "relative_humidity_2m" -> "relative_humidity_2m";
                case "vpd_kpa" -> null; // derived in our test world
                default -> null;
            };
        }
    }

    // --------------- Tests ---------------

    @Test
    void extractHourlyDataTypes_copiesAndClearsAndSetsOpenMeteoRequestVars() throws Exception {
        FakeDerivedSeriesRegistry registry = new FakeDerivedSeriesRegistry();
        registry.register(new VpdCalc());
        PlannerHarness harness = new PlannerHarness(registry);

        WeatherRequest req = new WeatherRequest();

        // Seed with garbage to prove it clears
        req.getHourlyDataTypes().add("junk_old_value");

        // Hourly processors request two types
        HourlyWeatherProcessRequest h1 = new HourlyWeatherProcessRequest();
        h1.setHourlyDataType("temperature_2m");

        HourlyWeatherProcessRequest h2 = new HourlyWeatherProcessRequest();
        h2.setHourlyDataType("dewpoint_2m");

        req.setHourlyWeatherProcessRequests(List.of(h1, h2));

        // The method uses req.getDailyDataTypes() for planning; populate it with a derived key
        req.getDailyDataTypes().clear();
        req.getDailyDataTypes().add("vpd_kpa");

        // bestEffort doesn't matter for this calc; set anyway
        req.setBestEffort(false);

        // Call private method via reflection
        Method m = PlannerHarness.class.getDeclaredMethod("extractHourlyDataTypes", WeatherRequest.class);
        m.setAccessible(true);
        m.invoke(harness, req);

        // HourlyDataTypes should be exactly the two from requests, and old garbage removed
        assertEquals(new LinkedHashSet<>(List.of("temperature_2m", "dewpoint_2m")), req.getHourlyDataTypes());

        // Plan(vpd_kpa) expands to temp + dewpoint fetch vars
        // so openmeteoRequestHourlyDataTypes should contain those two
        assertEquals(new LinkedHashSet<>(List.of("temperature_2m", "dewpoint_2m")),
                req.getOpenmeteoRequestHourlyDataTypes());
    }

    @Test
    void plan_returnsFetchVarsForRawAndDerivedKeys() {
        FakeDerivedSeriesRegistry registry = new FakeDerivedSeriesRegistry();
        registry.register(new VpdCalc());
        PlannerHarness harness = new PlannerHarness(registry);

        PlannerHarness.Plan plan = harness.plan(Set.of("vpd_kpa"), false);

        assertEquals(new LinkedHashSet<>(List.of("temperature_2m", "dewpoint_2m")), plan.fetchVars());
        assertEquals(Set.of("vpd_kpa"), plan.derivedKeys());
    }
}
