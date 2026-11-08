
package com.example.FruitTrees.NOAA;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.WeatherUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
@Component
public class NoaaStationFinder {
    private static final Logger LOGGER =
            Logger.getLogger(NoaaStationFinder.class.getName());
    private static final String DATASET = "global-hourly";
    private static final double MILES_PER_LATITUDE_DEGREE = 69.0;
    private static final double DEFAULT_RADIUS_MILES = 25.0;
    private static final int PAGE_SIZE = 1000;
    private static final int MAX_PAGES = 20;
    private static final Set<String> DEFAULT_DATA_TYPES =
            Set.of("TMP", "DEW");
    @Value("${noaa.station.url-hourly:https://www.ncei.noaa.gov/access/services/search/v1/data}")
    private String searchUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Finds the best NOAA station independently for each
     * default weather data type.
     *
     * Returns an empty or partial map when some data types
     * have no qualifying station.
     */
    public Map<String, NOAAStation> findBestStation(
            WeatherRequest request) {
        Objects.requireNonNull(request, "WeatherRequest is required");
        Location location = Objects.requireNonNull(
                request.getLocation(),
                "Location is required"
        );
        Set<String> requiredTypes = normalizeDataTypes(DEFAULT_DATA_TYPES);
        Map<String, NOAAStation> selectedStations =
                new LinkedHashMap<>();
        // 1. A global station ID overrides everything.
        String globalStationId = request.getLocation().getNoaaStationId();
        if (globalStationId != null && !globalStationId.isBlank()) {
            String stationId = globalStationId.trim();
            for (String type : requiredTypes) {
                selectedStations.put(
                        type,
                        new NOAAStation(
                                stationId,
                                location.getLatitude(),
                                location.getLongitude(),
                                null,
                                0.0d,
                                0
                        )
                );
            }
            return selectedStations;
        }
        // 2. Preserve stations explicitly selected per data type.
        Map<String, NOAAStation> suppliedStations =
                request.getLocation().getNoaaStations();
        if (suppliedStations != null) {
            suppliedStations.forEach((type, station) -> {
                if (type == null || station == null
                        || station.getStationId() == null
                        || station.getStationId().isBlank()) {
                    return;
                }
                String normalizedType =
                        type.trim().toUpperCase(Locale.ROOT);
                if (requiredTypes.contains(normalizedType)) {
                    selectedStations.put(normalizedType, station);
                }
            });
        }
        // 3. Only search for types that have no supplied station.
        Set<String> missingTypes = new LinkedHashSet<>(requiredTypes);
        missingTypes.removeAll(selectedStations.keySet());
        if (missingTypes.isEmpty()) {
            return selectedStations;
        }
        Map<String, NOAAStation> discoveredStations =
                findNearestStation(
                        location.getLatitude(),
                        location.getLongitude(),
                        DEFAULT_RADIUS_MILES,
                        request.getStartDate(),
                        request.getEndDate(),
                        missingTypes,
                        NOAAStationType.HOURLY
                );
        selectedStations.putAll(discoveredStations);
        return selectedStations;
    }
    /**
     * Finds the best NOAA Global Hourly station for each
     * requested data type.
     *
     * Each type is ranked independently:
     *
     * 1. Most distinct years represented.
     * 2. Shortest distance.
     * 3. Station ID for deterministic ties.
     *
     * A station can be selected for multiple data types.
     *
     * Years represented do not guarantee complete
     * hourly observations.
     */
    public Map<String, NOAAStation> findNearestStation(
            double latitude,
            double longitude,
            double radiusMiles,
            String startDate,
            String endDate,
            Set<String> dataTypes,
            NOAAStationType stationType) {
        validateInputs(
                latitude,
                longitude,
                radiusMiles,
                startDate,
                endDate,
                dataTypes,
                stationType
        );
        Set<String> requiredTypes = normalizeDataTypes(dataTypes);
        String bbox = getBoundingBox(
                radiusMiles,
                latitude,
                longitude
        );
        // Each NOAA data type maintains its own candidates.
        Map<String, Map<String, Candidate>> candidatesByType =
                new LinkedHashMap<>();
        for (String type : requiredTypes) {
            candidatesByType.put(type, new HashMap<>());
        }
        for (int page = 0; page < MAX_PAGES; page++) {
            int offset = page * PAGE_SIZE;
            URI uri = UriComponentsBuilder
                    .fromUriString(searchUrl)
                    .queryParam("dataset", DATASET)
                    .queryParam("bbox", bbox)
                    .queryParam("startDate", startDate)
                    .queryParam("endDate", endDate)
                    .queryParam(
                            "dataTypes",
                            String.join(",", requiredTypes)
                    )
                    .queryParam("limit", PAGE_SIZE)
                    .queryParam("offset", offset)
                    .build()
                    .encode()
                    .toUri();
            LOGGER.info(
                    "Searching NOAA Global Hourly stations: " + uri
            );
            String response =
                    restTemplate.getForObject(uri, String.class);
            if (response == null || response.isBlank()) {
                throw new IllegalStateException(
                        "Empty NOAA search response"
                );
            }
            JsonNode root;
            try {
                root = objectMapper.readTree(response);
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Unable to parse NOAA search response",
                        e
                );
            }
            JsonNode results = root.path("results");
            if (!results.isArray()) {
                throw new IllegalStateException(
                        "NOAA search response has no results array"
                );
            }
            for (JsonNode record : results) {
                JsonNode coordinates =
                        record.path("location").path("coordinates");
                if (!coordinates.isArray()
                        || coordinates.size() < 2
                        || !coordinates.get(0).isNumber()
                        || !coordinates.get(1).isNumber()) {
                    continue;
                }
                // GeoJSON order: longitude, latitude.
                double stationLongitude =
                        coordinates.get(0).asDouble();
                double stationLatitude =
                        coordinates.get(1).asDouble();
                double distance = WeatherUtilities.distanceMiles(
                        latitude,
                        longitude,
                        stationLatitude,
                        stationLongitude
                );
                if (!Double.isFinite(distance)
                        || distance > radiusMiles) {
                    continue;
                }
                String year = extractYear(
                        record.path("startDate").asText("")
                );
                JsonNode stations = record.path("stations");
                if (!stations.isArray()) {
                    continue;
                }
                for (JsonNode station : stations) {
                    String stationId =
                            station.path("id").asText("");
                    if (stationId.isBlank()) {
                        continue;
                    }
                    Set<String> availableTypes =
                            getAvailableDataTypes(station);
                    Double elevationMeters =
                            extractElevation(station, record);
                    // Evaluate each requested type independently.
                    for (String type : requiredTypes) {
                        if (!availableTypes.contains(type)) {
                            continue;
                        }
                        Map<String, Candidate> candidates =
                                candidatesByType.get(type);
                        Candidate candidate =
                                candidates.get(stationId);
                        if (candidate == null) {
                            candidate = new Candidate(
                                    stationId,
                                    stationLatitude,
                                    stationLongitude,
                                    elevationMeters,
                                    distance
                            );
                            candidates.put(stationId, candidate);
                        } else {
                            if (distance < candidate.distanceMiles) {
                                candidate.distanceMiles = distance;
                                candidate.latitude = stationLatitude;
                                candidate.longitude = stationLongitude;
                            }
                            if (candidate.elevationMeters == null
                                    && elevationMeters != null) {
                                candidate.elevationMeters =
                                        elevationMeters;
                            }
                        }
                        if (year != null) {
                            candidate.years.add(year);
                        }
                    }
                }
            }
            if (results.size() < PAGE_SIZE) {
                break;
            }
            if (page == MAX_PAGES - 1) {
                throw new IllegalStateException(
                        "NOAA search exceeded "
                                + MAX_PAGES
                                + " pages; cannot rank complete results"
                );
            }
        }
        Map<String, NOAAStation> selectedStations =
                new LinkedHashMap<>();
        Comparator<Candidate> ranking =
                Comparator
                        .comparingInt(
                                (Candidate c) -> c.years.size()
                        )
                        .reversed()
                        .thenComparingDouble(
                                c -> c.distanceMiles
                        )
                        .thenComparing(c -> c.id);
        for (String type : requiredTypes) {
            Candidate best = candidatesByType.get(type)
                    .values()
                    .stream()
                    .min(ranking)
                    .orElse(null);
            if (best == null) {
                LOGGER.info(
                        "No NOAA station within "
                                + radiusMiles
                                + " miles reports type: "
                                + type
                );
                continue;
            }
            NOAAStation selectedStation = new NOAAStation(
                    best.id,
                    best.latitude,
                    best.longitude,
                    best.elevationMeters,
                    best.distanceMiles,
                    best.years.size()
            );
            selectedStations.put(type, selectedStation);
            LOGGER.info(
                    "Selected NOAA station for "
                            + type + ": "
                            + selectedStation.getStationId()
                            + " ("
                            + selectedStation.getYearsAvailable()
                            + " years represented, "
                            + selectedStation.getDistanceMiles()
                            + " miles away, elevation "
                            + selectedStation.getElevationMeters()
                            + " meters)"
            );
        }
        return selectedStations;
    }
    /**
     * Validate search parameters.
     */
    private void validateInputs(
            double latitude,
            double longitude,
            double radiusMiles,
            String startDate,
            String endDate,
            Set<String> dataTypes,
            NOAAStationType stationType) {
        if (stationType == null) {
            throw new IllegalArgumentException(
                    "Station type is required"
            );
        }
        if (stationType == NOAAStationType.DAILY) {
            throw new UnsupportedOperationException(
                    "Daily NOAA station discovery is not supported"
            );
        }
        if (!Double.isFinite(latitude)
                || latitude < -90
                || latitude > 90
                || !Double.isFinite(longitude)
                || longitude < -180
                || longitude > 180
                || !Double.isFinite(radiusMiles)
                || radiusMiles <= 0) {
            throw new IllegalArgumentException(
                    "Invalid coordinates or radius"
            );
        }
        LocalDate from = LocalDate.parse(startDate);
        LocalDate to = LocalDate.parse(endDate);
        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "Start date is after end date"
            );
        }
        if (dataTypes == null || dataTypes.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one NOAA data type is required"
            );
        }
        if (normalizeDataTypes(dataTypes).isEmpty()) {
            throw new IllegalArgumentException(
                    "No valid NOAA data types provided"
            );
        }
    }
    /**
     * Normalize NOAA data type identifiers.
     */
    private Set<String> normalizeDataTypes(
            Set<String> dataTypes) {
        Set<String> normalized = new LinkedHashSet<>();
        for (String dataType : dataTypes) {
            if (dataType == null || dataType.isBlank()) {
                continue;
            }
            normalized.add(
                    dataType.trim().toUpperCase(Locale.ROOT)
            );
        }
        return normalized;
    }
    /**
     * Return the NOAA data types reported by a station.
     */
    private Set<String> getAvailableDataTypes(
            JsonNode station) {
        JsonNode types = station.path("dataTypes");
        if (!types.isArray()) {
            return Collections.emptySet();
        }
        Set<String> availableTypes = new HashSet<>();
        for (JsonNode type : types) {
            String id = type.path("id").asText("");
            if (!id.isBlank()) {
                availableTypes.add(
                        id.trim().toUpperCase(Locale.ROOT)
                );
            }
        }
        return availableTypes;
    }
    /**
     * Extract the year from an ISO date string.
     */
    private String extractYear(String date) {
        if (date == null || date.length() < 4) {
            return null;
        }
        String year = date.substring(0, 4);
        for (int i = 0; i < 4; i++) {
            if (!Character.isDigit(year.charAt(i))) {
                return null;
            }
        }
        return year;
    }
    /**
     * Extract optional station elevation.
     *
     * Verify elevation units against the NOAA
     * response schema before using as meters.
     */
    private Double extractElevation(
            JsonNode station,
            JsonNode record) {
        JsonNode elevation = station.path("elevation");
        if (!elevation.isNumber()) {
            elevation = record.path("elevation");
        }
        if (!elevation.isNumber()) {
            return null;
        }
        double value = elevation.asDouble();
        return Double.isFinite(value) ? value : null;
    }
    /**
     * NOAA Access Search bounding-box order:
     * north, west, south, east.
     */
    private String getBoundingBox(
            double radiusMiles,
            double latitude,
            double longitude) {
        double latDelta =
                radiusMiles / MILES_PER_LATITUDE_DEGREE;
        double maxLatitude =
                Math.min(90.0, latitude + latDelta);
        double minLatitude =
                Math.max(-90.0, latitude - latDelta);
        double cosine =
                Math.cos(Math.toRadians(latitude));
        double lonDelta =
                Math.abs(cosine) < 0.000001
                        ? 180.0
                        : Math.min(
                        180.0,
                        radiusMiles
                                / (MILES_PER_LATITUDE_DEGREE
                                * Math.abs(cosine))
                );
        return String.format(
                Locale.ROOT,
                "%.6f,%.6f,%.6f,%.6f",
                maxLatitude,
                Math.max(-180.0, longitude - lonDelta),
                minLatitude,
                Math.min(180.0, longitude + lonDelta)
        );
    }
    /**
     * Internal mutable candidate used while aggregating
     * NOAA search results.
     */
    private static final class Candidate {
        private final String id;
        private double latitude;
        private double longitude;
        private Double elevationMeters;
        private double distanceMiles;
        private final Set<String> years = new HashSet<>();
        private Candidate(
                String id,
                double latitude,
                double longitude,
                Double elevationMeters,
                double distanceMiles) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.elevationMeters = elevationMeters;
            this.distanceMiles = distanceMiles;
        }
    }
}
