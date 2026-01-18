package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;

import java.time.LocalDateTime;

public record TimeValue(LocalDateTime v) implements Value {
    @Override public Object raw() { return v; }
}
