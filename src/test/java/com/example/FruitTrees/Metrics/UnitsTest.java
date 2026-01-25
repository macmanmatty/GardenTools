package com.example.FruitTrees.Metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitsTest {

    private static final double EPS = 1e-9;

    @Test
    void convert_temperature_degC_to_degF() {
        // 0C = 32F
        double out = Units.convert(0.0, Unit.DEG_C, Unit.DEG_F);
        assertEquals(32.0, out, EPS);
    }

    @Test
    void convert_temperature_degF_to_K() {
        // 32F = 273.15K
        double out = Units.convert(32.0, Unit.DEG_F, Unit.DEG_K);
        assertEquals(273.15, out, 1e-6);
    }

    @Test
    void convert_wind_mps_to_mph() {
        // 1 m/s = 2.236936 mph
        double out = Units.convert(1.0, Unit.M_PER_S, Unit.MPH);
        assertEquals(2.236936, out, 1e-6);
    }

    @Test
    void convert_pressure_hpa_to_kpa() {
        // 1013.25 hPa = 101.325 kPa
        double out = Units.convert(1013.25, Unit.HPA, Unit.KPA);
        assertEquals(101.325, out, 1e-6);
    }

    @Test
    void convert_pressure_inhg_to_hpa() {
        // Standard sea level pressure: 29.92 inHg ≈ 1013.25 hPa
        double out = Units.convert( 29.92, Unit.INHG, Unit.HPA);
        assertEquals(1013.25, out, 0.8); // allow a little slack due to constant rounding
    }

    @Test
    void convert_depth_mm_to_in() {
        // 25.4 mm = 1 inch
        double out = Units.convert( 25.4, Unit.MM, Unit.IN);
        assertEquals(1.0, out, EPS);
    }

    @Test
    void convert_returnsSameValueForSameUnit() {
        double out = Units.convert(123.456, Unit.HPA, Unit.HPA);
        assertEquals(123.456, out, EPS);
    }

    @Test
    void convert_throwsForUnsupportedQuantityType() {
        assertThrows(IllegalArgumentException.class, () ->
                Units.convert( 10.0, Unit.M, Unit.W_PER_M2)
        );
    }

    @Test
    void convert_throwsForUnsupportedUnitPair() {
        // Trying to convert wind speed using pressure units should fail
        assertThrows(IllegalArgumentException.class, () ->
                Units.convert( 10.0, Unit.HPA, Unit.MPH)
        );
    }
}
