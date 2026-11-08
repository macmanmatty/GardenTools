
package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@Service
public class NOAAHTTPRequest {

    private static final Logger LOGGER =
            Logger.getLogger(NOAAHTTPRequest.class.getName());

    private final RestTemplate restTemplate = new RestTemplate();
    private final NOAAHourlyNormalizer normalizer;
    private final RateLimiter limiter;
    private final String noaaUrl;

    public NOAAHTTPRequest(
            NOAAHourlyNormalizer normalizer,
            @Value("${noaa.api.rate-limit}") double rateLimit,
            @Value("${noaa.url}") String noaaUrl) {

        this.normalizer = normalizer;
        this.limiter = RateLimiter.create(rateLimit);
        this.noaaUrl = noaaUrl;
    }

    /**
     * Retrieves NOAA hourly observations using the stations
     * already selected for each requested data type.
     */
    @Cacheable(
            value = "noaaDataCache",
            key = "#location.getLatitude() + ':'"
                    + " + #location.getLongitude() + ':'"
                    + " + #location.getNoaaStations().toString() + ':'"
                    + " + #weatherRequest.getHourlyDataTypes().toString() + ':'"
                    + " + #weatherRequest.getStartDate() + ':'"
                    + " + #weatherRequest.getEndDate() + ':'"
                    + " + #weatherRequest.getTemperatureUnit()"
    )
    public NOAAHourlyDataMap makeLocationRequest(
            Location location,
            WeatherRequest weatherRequest) {

        Objects.requireNonNull(location, "Location is required");
        Objects.requireNonNull(weatherRequest, "WeatherRequest is required");

        LocalDate startDate = LocalDate.parse(
                weatherRequest.getStartDate().toString()
        );

        LocalDate endDate = LocalDate.parse(
                weatherRequest.getEndDate().toString()
        );

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "NOAA start date must not be after end date"
            );
        }

        Map<String, NOAAStation> stations =
                location.getNoaaStations();

        if (stations == null || stations.isEmpty()) {
            throw new IllegalArgumentException(
                    "No NOAA stations selected"
            );
        }

        // Group measurements by station ID to avoid
        // requesting the same station multiple times.
        Map<String, Set<String>> stationDataTypes =
                groupStations(stations, weatherRequest);

        List<NOAAWeatherRecord> allData = new ArrayList<>();

        boolean useFahrenheit =
                !isCelsius(weatherRequest.temperatureUnit);

        for (Map.Entry<String, Set<String>> entry
                : stationDataTypes.entrySet()) {

            String stationId = entry.getKey();
            Set<String> dataTypes = entry.getValue();

            LocalDate chunkStart = startDate;

            while (!chunkStart.isAfter(endDate)) {

                LocalDate chunkEnd = chunkStart
                        .withDayOfYear(1)
                        .plusYears(1)
                        .minusDays(1);

                if (chunkEnd.isAfter(endDate)) {
                    chunkEnd = endDate;
                }

                fetchDateRange(
                        stationId,
                        dataTypes,
                        chunkStart,
                        chunkEnd,
                        useFahrenheit,
                        allData
                );

                chunkStart = chunkEnd.plusDays(1);
            }
        }

        NOAAHourlyDataMap result = new NOAAHourlyDataMap();
        result.addRecords(allData);

        normalizer.normalize(result, startDate, endDate);

        return result;
    }

    /**
     * Groups requested data types by their selected station.
     *
     * Example:
     * TMP -> Station A
     * DEW -> Station A
     * WND -> Station B
     *
     * Produces:
     * Station A -> [TMP, DEW]
     * Station B -> [WND]
     */
    private Map<String, Set<String>> groupStations(
            Map<String, NOAAStation> stations,
            WeatherRequest request) {

        Map<String, Set<String>> grouped =
                new LinkedHashMap<>();

        Set<String> requestedTypes = new LinkedHashSet<>();

        if (request.getHourlyDataTypes() != null) {
            for (String type : request.getHourlyDataTypes()) {

                if (type == null || type.isBlank()) {
                    continue;
                }

                String noaaType =
                        DataUtilities.toNOAADatatype(type);

                if (noaaType != null && !noaaType.isBlank()) {
                    requestedTypes.add(
                            noaaType.trim().toUpperCase(Locale.ROOT)
                    );
                }
            }
        }

        if (requestedTypes.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one NOAA data type is required"
            );
        }

        Map<String, NOAAStation> normalizedStations =
                new HashMap<>();

        stations.forEach((type, station) -> {
            if (type != null) {
                normalizedStations.put(
                        type.trim().toUpperCase(Locale.ROOT),
                        station
                );
            }
        });

        for (String type : requestedTypes) {

            NOAAStation station = normalizedStations.get(type);

            if (station == null
                    || station.getStationId() == null
                    || station.getStationId().isBlank()) {

                LOGGER.warning(
                        "No NOAA station selected for " + type
                );

                continue;
            }

            String stationId = station.getStationId().trim();

            grouped.computeIfAbsent(
                    stationId,
                    key -> new LinkedHashSet<>()
            ).add(type);
        }

        return grouped;
    }

    /**
     * Fetches the assigned data types from one station
     * for a single calendar-year chunk.
     */
    private void fetchDateRange(
            String stationId,
            Set<String> dataTypes,
            LocalDate startDate,
            LocalDate endDate,
            boolean useFahrenheit,
            List<NOAAWeatherRecord> allData) {

        URI uri = buildNoaaUrl(
                stationId,
                dataTypes,
                startDate,
                endDate
        );

        limiter.acquire();

        LOGGER.info(
                "NOAA request: station=" + stationId
                        + ", types=" + dataTypes
                        + ", start=" + startDate
                        + ", end=" + endDate
        );

        ResponseEntity<NOAAGlobalHourlyRecord[]> response =
                restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        NOAAGlobalHourlyRecord[].class
                );

        NOAAGlobalHourlyRecord[] results = response.getBody();

        if (results == null) {
            throw new IllegalStateException(
                    "NOAA returned an empty response body"
            );
        }

        for (NOAAGlobalHourlyRecord record : results) {
            allData.addAll(
                    convertRecord(record, dataTypes, useFahrenheit)
            );
        }

        LOGGER.info(
                "Retrieved " + results.length
                        + " observations from " + stationId
        );
    }

    /**
     * Converts only the data types assigned to this station.
     */
    private List<NOAAWeatherRecord> convertRecord(
            NOAAGlobalHourlyRecord raw,
            Set<String> dataTypes,
            boolean useFahrenheit) {

        List<NOAAWeatherRecord> records = new ArrayList<>();

        if (dataTypes.contains("TMP")) {
            addMeasurement(
                    records,
                    raw,
                    "TMP",
                    raw.getTemperature(),
                    useFahrenheit
            );
        }

        if (dataTypes.contains("DEW")) {
            addMeasurement(
                    records,
                    raw,
                    "DEW",
                    raw.getDewPoint(),
                    useFahrenheit
            );
        }

        return records;
    }

    /**
     * Decodes a NOAA temperature or dew point observation.
     */
    private void addMeasurement(
            List<NOAAWeatherRecord> records,
            NOAAGlobalHourlyRecord raw,
            String dataType,
            String encodedValue,
            boolean useFahrenheit) {

        if (encodedValue == null || encodedValue.isBlank()) {
            return;
        }

        String[] parts = encodedValue.split(",");

        if (parts.length < 2) {
            return;
        }

        try {
            int encoded = Integer.parseInt(parts[0].trim());

            if (Math.abs(encoded) == 9999) {
                return;
            }

            String qualityFlag = parts[1].trim();

            if (!qualityFlag.equals("1")
                    && !qualityFlag.equals("5")) {
                return;
            }

            double value = encoded / 10.0;

            if (useFahrenheit) {
                value = value * 9.0 / 5.0 + 32.0;
            }

            NOAAWeatherRecord record = new NOAAWeatherRecord();

            record.setDate(raw.getDate());
            record.setStation(raw.getStation());
            record.setDatatype(dataType);
            record.setValue(value);
            record.setAttributes(qualityFlag);

            records.add(record);

        } catch (NumberFormatException e) {
            LOGGER.warning(
                    "Invalid NOAA " + dataType
                            + " value: " + encodedValue
            );
        }
    }

    /**
     * Builds the NOAA request for one station and its
     * assigned data types.
     */
    private URI buildNoaaUrl(
            String stationId,
            Set<String> dataTypes,
            LocalDate startDate,
            LocalDate endDate) {

        return UriComponentsBuilder
                .fromUriString(noaaUrl)
                .queryParam("dataset", "global-hourly")
                .queryParam("stations", stationId)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("dataTypes", String.join(",", dataTypes))
                .queryParam("format", "json")
                .build()
                .encode()
                .toUri();
    }

    private boolean isCelsius(String unit) {
        return unit != null
                && (unit.equalsIgnoreCase("celsius")
                || unit.equalsIgnoreCase("c"));
    }
}
