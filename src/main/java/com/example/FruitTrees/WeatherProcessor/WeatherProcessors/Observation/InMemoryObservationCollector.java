package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Simple in-memory implementation of ObservationCollector.
 *
 * This is a per-run, ephemeral ledger that collects all Observation objects
 * produced by weather processors during a single calculation run.
 *
 * It performs NO aggregation, NO formatting, and NO persistence.
 * Its only responsibility is to store immutable Observation records
 * in the order they are generated and make them available for export
 * (Excel, PDF, database, etc.).
 *
 * In production, this may later be replaced or complemented by:
 *  - a database-backed collector
 *  - a streaming collector
 *  - a message-queue publisher
 */
@Component("InMemoryObservationCollector")
public class InMemoryObservationCollector implements ObservationCollector {

    /**
     * Internal list holding all collected observations for the current run.
     * This list grows only for the lifetime of a single WeatherRunContext.
     */
    private final List<Observation> observations = new ArrayList<>();

    /**
     * Adds a single immutable Observation to the collector.
     * Called by weather processors when they finish computing a fact
     * (e.g., yearly chill, monthly hours below threshold, mean temperature).
     */
    @Override
    public void add(Observation observation) {
        observations.add(observation);
    }

    /**
     * Returns all observations accumulated for this run.
     * The returned list represents the complete scientific ledger
     * for one location and one calculation version.
     */
    @Override
    public List<Observation> getAll() {
        return observations;
    }
}
