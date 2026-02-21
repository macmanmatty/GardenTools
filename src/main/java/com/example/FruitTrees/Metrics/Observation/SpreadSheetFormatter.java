package com.example.FruitTrees.Metrics.Observation;

import com.example.FruitTrees.Metrics.Comparison;
import com.example.FruitTrees.Metrics.Condition;

public class SpreadSheetFormatter implements ObservationFormatter {

        @Override
        public String toColumnHeader(Observation o) {
            Condition c = o.condition();
            String resultUnit = o.canonicalUnit().pretty(); // "Hours"
            String variableUnit = c.unit().pretty(); // "°F"
            String comparison = c.comparison().description(); // "greater than", "between"

            if (c.comparison() == Comparison.BETWEEN) {
                return String.format("%s of %s %s %s–%s",
                        resultUnit,
                        variableUnit,
                        comparison,
                        c.lowerBound(),
                        c.upperBound());
            }

            return String.format("%s of %s %s %s",
                    resultUnit,
                    variableUnit,
                    comparison,
                    c.threshold());
        }
}
