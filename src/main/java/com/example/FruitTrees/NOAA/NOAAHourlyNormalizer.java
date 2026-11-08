
package com.example.FruitTrees.NOAA;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
/**
 * Normalizes NOAA observations into continuous, hourly weather data.
 *
 * Every requested hour is represented, including hours without observations.
 * Missing values are stored as Double.NaN.
 *
 * Multiple observations for the same data type and hour are averaged.
 * All timestamps are interpreted in UTC.
 */
@Component
public class NOAAHourlyNormalizer {
    /**
     * Normalizes weather observations over the complete requested date range.
     *
     * The timeline begins at 00:00 on startDate and ends at 23:00 on endDate.
     * It does not depend on the first or last available NOAA observation.
     *
     * @param dataMap NOAA observations to normalize
     * @param startDate first requested date, inclusive
     * @param endDate last requested date, inclusive
     */
    public void normalize(
            NOAAHourlyDataMap dataMap,
            LocalDate startDate,
            LocalDate endDate) {
        Objects.requireNonNull(dataMap, "Data map is required");
        Objects.requireNonNull(startDate, "Start date is required");
        Objects.requireNonNull(endDate, "End date is required");
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date must not be after end date");
        }
        // Establish the complete requested hourly timeline.
        LocalDateTime firstHour = startDate.atStartOfDay();
        LocalDateTime lastHour = endDate.atTime(23, 0);
        int numberOfHours = Math.toIntExact(
                ChronoUnit.HOURS.between(firstHour, lastHour) + 1
        );
        LocalDateTime[] normalizedTime =
                new LocalDateTime[numberOfHours];
        for (int i = 0; i < numberOfHours; i++) {
            normalizedTime[i] = firstHour.plusHours(i);
        }
        // Retrieve observations grouped by NOAA data type.
        Map<String, List<NOAAWeatherRecord>> observations =
                dataMap.getNoaaHourlyObservationsMap();
        // Group finite observations by data type and UTC hour.
        Map<String, Map<LocalDateTime, DoubleSummaryStatistics>> grouped =
                new HashMap<>();
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
                // Exclude observations outside the requested date range.
                if (hour.isBefore(firstHour) || hour.isAfter(lastHour)) {
                    continue;
                }
                // Accumulate all observations belonging to this hour.
                hourly.computeIfAbsent(
                        hour,
                        key -> new DoubleSummaryStatistics()
                ).accept(value);
            }
            grouped.put(entry.getKey(), hourly);
        }
        // Align each data type to the same complete hourly timeline.
        Map<String, double[]> normalizedData = new HashMap<>();
        for (Map.Entry<String,
                Map<LocalDateTime, DoubleSummaryStatistics>> entry :
                grouped.entrySet()) {
            double[] values = new double[numberOfHours];
            // Missing observations must not be mistaken for zero.
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
        // Save the complete timeline and aligned weather arrays.
        dataMap.setNormalizedData(normalizedTime, normalizedData);
    }
    /**
     * Parses a NOAA observation timestamp as UTC.
     *
     * @param date NOAA timestamp
     * @return UTC timestamp without timezone information
     */
    private LocalDateTime parseNoaaTime(String date) {
        String timestamp =
                date.endsWith("Z") ? date : date + "Z";
        return LocalDateTime.ofInstant(
                Instant.parse(timestamp),
                ZoneOffset.UTC
        );
    }
}
