package com.example.FruitTrees.Metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
    void fromString_trimsAndIgnoresCase() {
        assertEquals(Unit.MPH, Unit.fromString("mph"));
        assertEquals(Unit.MPH, Unit.fromString(" MPH "));
        assertEquals(Unit.DEG_C, Unit.fromString("degc"));
        assertEquals(Unit.DEG_C, Unit.fromString("  DeGc  "));
        assertEquals(Unit.KPA, Unit.fromString("kPa"));
        assertEquals(Unit.W_PER_M2, Unit.fromString(" w/m2 "));
    }

    @Test
    void fromString_throwsOnUnknown() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Unit.fromString("bananasPerSecond")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("unknown"));
    }

    @Test
    void fromString_throwsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> Unit.fromString(null));
    }
}
