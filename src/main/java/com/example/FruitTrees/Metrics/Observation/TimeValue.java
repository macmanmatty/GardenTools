package com.example.FruitTrees.Metrics.Observation;

import java.time.LocalDateTime;

public record TimeValue(LocalDateTime v) implements Value {
    @Override public Object raw() { return v; }
}
