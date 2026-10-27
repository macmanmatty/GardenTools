package com.example.FruitTrees.NOAA;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class NOAAHourlyNormalizer{
    public void normalize(NOAAHourlyDataMap dataMap) {

        Map<String, List<NOAAWeatherRecord>> observations =
                dataMap.getNoaaHourlyObservationsMap();

        Map<String, Map<LocalDateTime, DoubleSummaryStatistics>> grouped =
                new HashMap<>();

        LocalDateTime firstHour = null;
        LocalDateTime lastHour = null;

        // Group observations by datatype and hour.
        for (Map.Entry<String, List<NOAAWeatherRecord>> entry :
                observations.entrySet()) {

            Map<LocalDateTime, DoubleSummaryStatistics> hourly =
                    new HashMap<>();

            for (NOAAWeatherRecord record : entry.getValue()) {

                if (record.getDate() == null) {
                    continue;
                }

                double value = record.getValue();

                if (!Double.isFinite(value)) {
                    continue;
                }

                LocalDateTime hour = parseNoaaTime(record.getDate())
                        .truncatedTo(ChronoUnit.HOURS);

                hourly.computeIfAbsent(
                        hour,
                        key -> new DoubleSummaryStatistics()
                ).accept(value);

                if (firstHour == null || hour.isBefore(firstHour)) {
                    firstHour = hour;
                }

                if (lastHour == null || hour.isAfter(lastHour)) {
                    lastHour = hour;
                }
            }

            grouped.put(entry.getKey(), hourly);
        }

        // Handle empty datasets.
        if (firstHour == null) {
            dataMap.setNormalizedData(
                    new LocalDateTime[0],
                    new HashMap<>()
            );
            return;
        }

        // Build a continuous hourly timeline.
        int numberOfHours = Math.toIntExact(
                ChronoUnit.HOURS.between(firstHour, lastHour) + 1
        );

        LocalDateTime[] normalizedTime =
                new LocalDateTime[numberOfHours];

        for (int i = 0; i < numberOfHours; i++) {
            normalizedTime[i] = firstHour.plusHours(i);
        }

        // Align each datatype to the same hourly timeline.
        Map<String, double[]> normalizedData = new HashMap<>();

        for (Map.Entry<String, Map<LocalDateTime, DoubleSummaryStatistics>> entry :
                grouped.entrySet()) {

            double[] values = new double[numberOfHours];
            Arrays.fill(values, Double.NaN);

            Map<LocalDateTime, DoubleSummaryStatistics> hourly =
                    entry.getValue();

            for (int i = 0; i < numberOfHours; i++) {

                DoubleSummaryStatistics stats =
                        hourly.get(normalizedTime[i]);

                if (stats != null && stats.getCount() > 0) {
                    values[i] = stats.getAverage();
                }
            }

            normalizedData.put(entry.getKey(), values);
        }

        // IMPORTANT: Save the normalized results back to the data map.
        dataMap.setNormalizedData(normalizedTime, normalizedData);
    }
    private LocalDateTime parseNoaaTime(String date) {

        String timestamp = date.endsWith("Z") ? date : date + "Z";

        return LocalDateTime.ofInstant(
                Instant.parse(timestamp),
                ZoneOffset.UTC
        );
    }
}
