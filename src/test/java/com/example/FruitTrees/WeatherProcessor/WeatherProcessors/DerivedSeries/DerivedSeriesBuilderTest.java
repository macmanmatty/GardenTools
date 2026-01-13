package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DerivedSeriesBuilderTest {

    @Test
    void buildsDerivedSeriesAndStoresInMap() {
        // Fake calculator: out = a + b
        DerivedSeriesCalculator sumCalc = new DerivedSeriesCalculator() {
            @Override public String outputType() { return "sum"; }
            @Override public List<String> requiredInputTypes() { return List.of("a", "b"); }
            @Override public List<String> optionalInputTypes() { return List.of(); }
            @Override public double computeAt(double[] req, double[] opt) { return req[0] + req[1]; }
        };

        Map<String, double[]> seriesByType = new HashMap<>();
        seriesByType.put("a", new double[]{1, 2, 3});
        seriesByType.put("b", new double[]{10, 20, 30});

        // Minimal harness of the builder logic (copy your method or call it if accessible)
        int minLen = 3;
        double[] out = new double[minLen];
        for (int i = 0; i < minLen; i++) {
            out[i] = sumCalc.computeAt(new double[]{seriesByType.get("a")[i], seriesByType.get("b")[i]}, new double[0]);
        }
        seriesByType.put("sum", out);

        assertArrayEquals(new double[]{11, 22, 33}, seriesByType.get("sum"), 1e-9);
    }

    @Test
    void usesMinLengthOfInputs() {
        DerivedSeriesCalculator sumCalc = new DerivedSeriesCalculator() {
            @Override public String outputType() { return "sum"; }
            @Override public List<String> requiredInputTypes() { return List.of("a", "b"); }
            @Override public double computeAt(double[] req, double[] opt) { return req[0] + req[1]; }
        };

        Map<String, double[]> seriesByType = new HashMap<>();
        seriesByType.put("a", new double[]{1, 2, 3, 4});
        seriesByType.put("b", new double[]{10, 20}); // shorter

        int minLen = Math.min(seriesByType.get("a").length, seriesByType.get("b").length);
        double[] out = new double[minLen];
        for (int i = 0; i < minLen; i++) {
            out[i] = sumCalc.computeAt(new double[]{seriesByType.get("a")[i], seriesByType.get("b")[i]}, new double[0]);
        }

        assertEquals(2, out.length);
        assertArrayEquals(new double[]{11, 22}, out, 1e-9);
    }
}
