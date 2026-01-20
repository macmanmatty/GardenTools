package com.example.FruitTrees.Metrics.Observation;


public sealed interface Value
        permits NumberValue, TimeValue, TextValue {
    Object raw();
}
