package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;

import java.time.LocalDateTime;

public sealed interface Value
        permits NumberValue, TimeValue, TextValue {
    Object raw();
}

