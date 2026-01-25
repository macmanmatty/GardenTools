
 package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculators.VpdCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    class VpdCalculatorTest {

        @Test
        void declaresMetadataCorrectly() {
            DerivedSeriesCalculator calc = new VpdCalculator();

            assertEquals("vpd_kpa", calc.outputType());
            assertEquals(List.of("temperature_2m_c", "dewpoint_2m_c"), calc.requiredInputTypes());
            assertEquals(List.of("leaf_temp_c"), calc.optionalInputTypes());
        }

        @Test
        void computesVpdKpa_forKnownCaseCelsiusInputs() {
            // Known values using common meteorological approximation:
            // T=30°C, Td=20°C => VPD ≈ 1.9048 kPa
            DerivedSeriesCalculator calc = new VpdCalculator();

            double[] required = {30.0, 20.0};
            double[] optional = {};

            double vpd = calc.computeAt(required, optional);

            assertEquals(1.9048, vpd, 1e-3);
            assertTrue(vpd >= 0.0, "VPD should not be negative for typical implementation");
        }

        @Test
        void computesZeroWhenTempEqualsDewpoint() {
            DerivedSeriesCalculator calc = new VpdCalculator();

            double vpd = calc.computeAt(new double[]{20.0, 20.0}, new double[0]);

            assertEquals(0.0, vpd, 1e-9);
        }
    }


