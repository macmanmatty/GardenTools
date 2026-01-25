package com.example.FruitTrees.Metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricRegistryTest {

    @Test
    void defFor_returnsKnownMetricDef() {
        MetricDef def = MetricRegistry.defFor("temperature_2m");

        // Adjust these if MetricDef uses getters instead of record accessors
        assertEquals("temperature_2m", def.dataType());
        assertEquals(Unit.DEG_C, def.canonicalUnit());
    }

    @Test
    void defFor_returnsUnknownFallback() {
        MetricDef def = MetricRegistry.defFor("totally_made_up_metric");

        assertEquals("totally_made_up_metric", def.dataType());
        assertEquals(Unit.UNKNOWN, def.canonicalUnit());
    }

    @Test
    void defFor_isCaseSensitiveByDesign() {
        // The registry keys are canonical ids; callers should normalize upstream if needed.
        MetricDef def = MetricRegistry.defFor("Temperature_2m");

        assertEquals("Temperature_2m", def.dataType());
        assertEquals(Unit.UNKNOWN, def.canonicalUnit());
    }
}
