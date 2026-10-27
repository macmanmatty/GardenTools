package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Utilities.WeatherUtilities;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class NoaaStationFinder {
    private static final Logger LOGGER = Logger.getLogger(NoaaStationFinder.class.getName());
    private static final String DATASET = "global-hourly";
    private static final double MILES_PER_LATITUDE_DEGREE = 69.0;
    private static final int PAGE_SIZE = 1000;
    private static final int MAX_PAGES = 20;

    @Value("${noaa.station.url-hourly:https://www.ncei.noaa.gov/access/services/search/v1/data}")
    private String searchUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** Returns a Global Hourly station ID, or null when none qualifies. */
    public String findNearestStation(
            double latitude,
            double longitude,
            double radiusMiles,
            String startDate,
            String endDate,
            String dataType,
            NOAAStationType stationType) {

        if (stationType == null) {
            throw new IllegalArgumentException("Station type is required");
        }
        if (stationType == NOAAStationType.DAILY) {
            throw new UnsupportedOperationException("Daily NOAA station discovery is not supported");
        }
        if (!Double.isFinite(latitude) || latitude < -90 || latitude > 90
                || !Double.isFinite(longitude) || longitude < -180 || longitude > 180
                || !Double.isFinite(radiusMiles) || radiusMiles <= 0) {
            throw new IllegalArgumentException("Invalid coordinates or radius");
        }
        LocalDate from = LocalDate.parse(startDate);
        LocalDate to = LocalDate.parse(endDate);
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Start date is after end date");
        }
        if (dataType == null || dataType.isBlank()) {
            throw new IllegalArgumentException("Data type is required");
        }

        String requiredType = dataType.trim().toUpperCase(Locale.ROOT);
        String bbox = getBoundingBox(radiusMiles, latitude, longitude);
        Map<String, Candidate> candidates = new HashMap<>();

        for (int page = 0; page < MAX_PAGES; page++) {
            int offset = page * PAGE_SIZE;
            URI uri = UriComponentsBuilder.fromUriString(searchUrl)
                    .queryParam("dataset", DATASET)
                    .queryParam("bbox", bbox)
                    .queryParam("startDate", startDate)
                    .queryParam("endDate", endDate)
                    .queryParam("dataTypes", requiredType)
                    .queryParam("limit", PAGE_SIZE)
                    .queryParam("offset", offset)
                    .build().encode().toUri();

            LOGGER.info("Searching NOAA Global Hourly stations: " + uri);
            String response = restTemplate.getForObject(uri, String.class);
            if (response == null || response.isBlank()) {
                throw new IllegalStateException("Empty NOAA search response");
            }

            JsonNode root;
            try {
                root = objectMapper.readTree(response);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to parse NOAA search response", e);
            }
            JsonNode results = root.path("results");
            if (!results.isArray()) {
                throw new IllegalStateException("NOAA search response has no results array");
            }

            for (JsonNode record : results) {
                JsonNode coordinates = record.path("location").path("coordinates");
                if (!coordinates.isArray() || coordinates.size() < 2
                        || !coordinates.get(0).isNumber() || !coordinates.get(1).isNumber()) {
                    continue;
                }
                // GeoJSON coordinates are [longitude, latitude].
                double stationLongitude = coordinates.get(0).asDouble();
                double stationLatitude = coordinates.get(1).asDouble();
                double distance = WeatherUtilities.distanceMiles(
                        latitude, longitude, stationLatitude, stationLongitude);
                if (distance > radiusMiles) {
                    continue;
                }

                String year = record.path("startDate").asText("");
                if (year.length() >= 4) {
                    year = year.substring(0, 4);
                }

                for (JsonNode station : record.path("stations")) {
                    String stationId = station.path("id").asText("");
                    if (stationId.isBlank() || !hasDataType(station, requiredType)) {
                        continue;
                    }
                    Candidate candidate = candidates.computeIfAbsent(
                            stationId, id -> new Candidate(id, distance));
                    candidate.distanceMiles = Math.min(candidate.distanceMiles, distance);
                    if (year.length() == 4) {
                        candidate.years.add(year);
                    }
                }
            }

            if (results.size() < PAGE_SIZE) {
                break;
            }
            if (page == MAX_PAGES - 1) {
                throw new IllegalStateException(
                        "NOAA search exceeded " + MAX_PAGES + " pages; cannot rank complete results");
            }
        }

        // Favor longer historical coverage; use distance to break ties.
        Candidate best = null;
        for (Candidate candidate : candidates.values()) {
            if (best == null
                    || candidate.years.size() > best.years.size()
                    || (candidate.years.size() == best.years.size()
                    && candidate.distanceMiles < best.distanceMiles)) {
                best = candidate;
            }
        }
        if (best == null) {
            LOGGER.info("No matching Global Hourly station within " + radiusMiles + " miles");
            return null;
        }
        LOGGER.info("Selected Global Hourly station " + best.id
                + " (" + best.years.size() + " years, "
                + best.distanceMiles + " miles away)");
        return best.id;
    }

    private boolean hasDataType(JsonNode station, String requiredType) {
        JsonNode types = station.path("dataTypes");
        if (!types.isArray()) {
            return false;
        }
        for (JsonNode type : types) {
            if (requiredType.equalsIgnoreCase(type.path("id").asText(""))) {
                return true;
            }
        }
        return false;
    }

    /** NOAA Access Search bbox order: north, west, south, east. */
    private String getBoundingBox(double radiusMiles, double latitude, double longitude) {
        double latDelta = radiusMiles / MILES_PER_LATITUDE_DEGREE;
        double maxLatitude = Math.min(90.0, latitude + latDelta);
        double minLatitude = Math.max(-90.0, latitude - latDelta);
        double cosine = Math.cos(Math.toRadians(latitude));
        double lonDelta = Math.abs(cosine) < 0.000001
                ? 180.0 : Math.min(180.0, radiusMiles / (MILES_PER_LATITUDE_DEGREE * Math.abs(cosine)));
        return String.format(Locale.ROOT, "%.6f,%.6f,%.6f,%.6f",
                maxLatitude, Math.max(-180.0, longitude - lonDelta),
                minLatitude, Math.min(180.0, longitude + lonDelta));
    }

    private static final class Candidate {
        private final String id;
        private double distanceMiles;
        private final Set<String> years = new HashSet<>();

        private Candidate(String id, double distanceMiles) {
            this.id = id;
            this.distanceMiles = distanceMiles;
        }
    }
}