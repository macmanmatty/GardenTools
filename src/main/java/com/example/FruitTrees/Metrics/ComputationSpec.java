package com.example.FruitTrees.Metrics;

import java.util.List;

public record ComputationSpec(
        String model,              // e.g. "utah_chill", "gdd_simple", "hours_above"
        Condition condition,        // optional (null when not applicable)
        List<Bin> bins              // optional (null/empty when not applicable)
) {}
