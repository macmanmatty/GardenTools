package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculator;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RelativeHumidityPlanningTest {

    // ---- Fake registry ----
    static class FakeDerivedSeriesRegistry {
        private final Map<String, DerivedSeriesCalculator> calcs = new HashMap<>();
        DerivedSeriesCalculator getCalculatorOrNull(String key) { return calcs.get(key); }
        void register(DerivedSeriesCalculator c) { calcs.put(c.outputType(), c); }
    }

    // ---- Fake calculators ----

    /** rh_2m_pct derived from temp + dewpoint */
    static class RhCalc implements DerivedSeriesCalculator {
        @Override public String outputType() { return "relative_humidity_2m"; } // use your canonical key here
        @Override public List<String> requiredInputTypes() { return List.of("temperature_2m", "dewpoint_2m"); }
        @Override public List<String> optionalInputTypes() { return List.of(); }
        @Override public double computeAt(double[] req, double[] opt) { return 0; }
    }

    /** vpd derived from temp + dewpoint */
    static class VpdCalc implements DerivedSeriesCalculator {
        @Override public String outputType() { return "vpd_kpa"; }
        @Override public List<String> requiredInputTypes() { return List.of("temperature_2m", "dewpoint_2m"); }
        @Override public List<String> optionalInputTypes() { return List.of(); }
        @Override public double computeAt(double[] req, double[] opt) { return 0; }
    }

    // ---- Controlled mapper (stand-in for DataUtilities.toOpenMeteoDatatype) ----
    static class DataUtilitiesShim {
        static String toOpenMeteoDatatype(String key) {
            if (key == null) return null;
            return switch (key) {
                case "temperature_2m" -> "temperature_2m";
                case "dewpoint_2m" -> "dewpoint_2m";
                case "relative_humidity_2m" -> "relative_humidity_2m";
                case "vpd_kpa" -> null; // derived
                default -> null;
            };
        }
    }

    // ---- Planner harness WITH the RH policy you want ----
    static class Planner {

        private final FakeDerivedSeriesRegistry registry;

        Planner(FakeDerivedSeriesRegistry registry) {
            this.registry = registry;
        }

        public record Plan(Set<String> fetchVars, Set<String> derivedKeys) {}

        public Plan plan(Set<String> requestedKeys, boolean bestEffort) {
            Set<String> fetchVars = new LinkedHashSet<>();
            Set<String> derived = new LinkedHashSet<>();

            // Policy: If RH is the ONLY requested output, fetch RH directly.
            boolean rhOnly = requestedKeys.size() == 1 && requestedKeys.contains("relative_humidity_2m");
            if (rhOnly) {
                fetchVars.add("relative_humidity_2m");
                return new Plan(fetchVars, derived);
            }

            // Otherwise, do the normal dependency expansion + derive where possible.
            Set<String> allKeysNeeded = expandKeys(requestedKeys, bestEffort);

            for (String key : allKeysNeeded) {
                DerivedSeriesCalculator calc = registry.getCalculatorOrNull(key);

                if (calc == null) {
                    String open = DataUtilitiesShim.toOpenMeteoDatatype(key);
                    if (open != null) fetchVars.add(open);
                    continue;
                }

                // If it's derived, compute it (your normal behavior)
                derived.add(key);

                // Ensure we fetch its required raw inputs
                for (String dep : calc.requiredInputTypes()) {
                    String open = DataUtilitiesShim.toOpenMeteoDatatype(dep);
                    if (open != null) fetchVars.add(open);
                }

                if (bestEffort) {
                    for (String opt : calc.optionalInputTypes()) {
                        String open = DataUtilitiesShim.toOpenMeteoDatatype(opt);
                        if (open != null) fetchVars.add(open);
                    }
                }
            }

            return new Plan(fetchVars, derived);
        }

        private Set<String> expandKeys(Set<String> requestedKeys, boolean bestEffort) {
            Set<String> out = new LinkedHashSet<>();
            Deque<String> stack = new ArrayDeque<>(requestedKeys);

            while (!stack.isEmpty()) {
                String key = stack.pop();
                if (!out.add(key)) continue;

                DerivedSeriesCalculator calc = registry.getCalculatorOrNull(key);
                if (calc == null) continue;

                stack.addAll(calc.requiredInputTypes());
                if (bestEffort) stack.addAll(calc.optionalInputTypes());
            }
            return out;
        }
    }

    @Test
    void whenOnlyRhRequested_weFetchRh_andDoNotFetchTempOrDewpoint() {
        FakeDerivedSeriesRegistry reg = new FakeDerivedSeriesRegistry();
        reg.register(new RhCalc()); // even though we CAN derive it, policy says fetch if RH-only

        Planner planner = new Planner(reg);

        Planner.Plan plan = planner.plan(Set.of("relative_humidity_2m"), false);

        assertEquals(Set.of("relative_humidity_2m"), plan.fetchVars());
        assertTrue(plan.derivedKeys().isEmpty(), "RH-only should not be derived under this policy");
    }

    @Test
    void whenVpdAndRhRequested_weDoNotFetchRh_weFetchTempAndDewpoint_andDeriveRh() {
        FakeDerivedSeriesRegistry reg = new FakeDerivedSeriesRegistry();
        reg.register(new RhCalc());
        reg.register(new VpdCalc());

        Planner planner = new Planner(reg);

        Planner.Plan plan = planner.plan(Set.of("vpd_kpa", "relative_humidity_2m"), false);

        // Should fetch only deps (temp+dew), not RH
        assertTrue(plan.fetchVars().contains("temperature_2m"));
        assertTrue(plan.fetchVars().contains("dewpoint_2m"));
        assertFalse(plan.fetchVars().contains("relative_humidity_2m"));

        // Should compute both derived outputs
        assertTrue(plan.derivedKeys().contains("vpd_kpa"));
        assertTrue(plan.derivedKeys().contains("relative_humidity_2m"));
    }
}
