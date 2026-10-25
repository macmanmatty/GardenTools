package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Utilities.WeatherUtilities;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Component
public class NoaaStationFinder {

    private static final double MILES_PER_LATITUDE_DEGREE = 69.0;

    @Value("${noaa.station.url}")
    private String noaaStationUrl;

    @Value("${noaa.api.key}")
    private String noaaApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Finds the nearest NOAA station of the requested type.
     *
     * DAILY:
     *   Searches for the nearest GHCN-Daily station.
     *
     * HOURLY:
     *   Searches for the nearest NOAA hourly station.
     *
     * FALL_BACK:
     *   Searches for an hourly station first. If no hourly station is
     *   available within the requested radius, searches for a daily station.
     *
     * @param latitude    target latitude
     * @param longitude   target longitude
     * @param radiusMiles maximum search radius in miles
     * @param startDate   requested start date
     * @param endDate     requested end date
     * @param dataType    requested NOAA data type
     * @param stationType type of NOAA station to search for
     *
     * @return nearest station ID, or null if no suitable station is found
     */
    public String findNearestStation(
            double latitude,
            double longitude,
            double radiusMiles,
            String startDate,
            String endDate,
            String dataType,
            NOAAStationType stationType) {

        if (stationType == null) {
            throw new IllegalArgumentException(
                    "NOAA station type cannot be null"
            );
        }

        return switch (stationType) {

            case HOURLY -> findNearestHourlyStation(
                    latitude,
                    longitude,
                    radiusMiles,
                    startDate,
                    endDate,
                    dataType
            );

            case DAILY -> findNearestDailyStation(
                    latitude,
                    longitude,
                    radiusMiles,
                    startDate,
                    endDate,
                    dataType
            );

            case FALL_BACK -> {

                // Prefer hourly observations whenever they are available.
                String stationId = findNearestHourlyStation(
                        latitude,
                        longitude,
                        radiusMiles,
                        startDate,
                        endDate,
                        dataType
                );

                // If no hourly station exists within the requested radius,
                // fall back to the nearest suitable daily station.
                if (stationId == null || stationId.isBlank()) {

                    stationId = findNearestDailyStation(
                            latitude,
                            longitude,
                            radiusMiles,
                            startDate,
                            endDate,
                            dataType
                    );
                }

                yield stationId;
            }
        };
    }
    /**
     * Finds the nearest GHCN-Daily station.
     */
    private String findNearestDailyStation(
            double latitude,
            double longitude,
            double radiusMiles,
            String startDate,
            String endDate,
            String dataType) {

        return findNearestStationForDataset(
                latitude,
                longitude,
                radiusMiles,
                startDate,
                endDate,
                dataType,
                "GHCND"
        );
    }

    /**
     * Finds the nearest NOAA hourly station.
     */
    private String findNearestHourlyStation(
            double latitude,
            double longitude,
            double radiusMiles,
            String startDate,
            String endDate,
            String dataType) {

        return findNearestStationForDataset(
                latitude,
                longitude,
                radiusMiles,
                startDate,
                endDate,
                dataType,
                "global-hourly"
        );
    }

   
    /**
     * Searches a specific NOAA dataset for the nearest station.
     */
    private String findNearestStationForDataset(
            double latitude,
            double longitude,
            double radiusMiles,
            String startDate,
            String endDate,
            String dataType,
            String datasetId) {

        try {

            String url = String.format(
                    "%s?datasetid=%s"
                            + "&extent=%s"
                            + "&datatypeid=%s"
                            + "&startdate=%s"
                            + "&enddate=%s"
                            + "&limit=1000",
                    noaaStationUrl,
                    datasetId,
                    getExtent(radiusMiles, latitude, longitude),
                    dataType,
                    startDate,
                    endDate
            );

            Logger.getLogger("").info(
                    "Searching NOAA " + datasetId + " stations: " + url
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("token", noaaApiKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode json =
                    objectMapper.readTree(response.getBody());

            JsonNode results =
                    json.get("results");

            if (results == null
                    || !results.isArray()
                    || results.isEmpty()) {

                return null;
            }

            return getClosestStation(
                    results,
                    latitude,
                    longitude,
                    radiusMiles
            );

        } catch (Exception e) {

            Logger.getLogger("")
                    .severe(
                            "Error finding NOAA "
                                    + datasetId
                                    + " station: "
                                    + e.getMessage()
                    );

            return null;
        }
    }
    /**
     * Creates the NOAA geographic extent (bounding box) used to search
     * for stations near the requested latitude/longitude.
     *
     * NOAA expects:
     *
     * southLatitude,westLongitude,northLatitude,eastLongitude
     *
     * Latitude is approximately 69 miles per degree. Longitude varies
     * with latitude, so its conversion is adjusted using cosine.
     */
    private String getExtent(
            double radiusMiles,
            double latitude,
            double longitude) {

        double latitudeDelta =
                radiusMiles / MILES_PER_LATITUDE_DEGREE;

        double longitudeDelta =
                radiusMiles /
                        (MILES_PER_LATITUDE_DEGREE *
                                Math.cos(Math.toRadians(latitude)));

        return String.format(
                "%f,%f,%f,%f",
                latitude - latitudeDelta,   // south
                longitude - longitudeDelta, // west
                latitude + latitudeDelta,   // north
                longitude + longitudeDelta  // east
        );
    }

    /**
     * Finds the closest station in the NOAA results.
     *
     * The NOAA extent search returns stations inside a rectangular
     * bounding box. Because the box can contain stations farther than
     * the requested radius, the actual great-circle distance is checked
     * before accepting a station.
     */
    private String getClosestStation(
            JsonNode results,
            double latitude,
            double longitude,
            double radiusMiles) {

        JsonNode closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (JsonNode station : results) {

            // Skip malformed station records.
            if (!station.hasNonNull("latitude")
                    || !station.hasNonNull("longitude")
                    || !station.hasNonNull("id")) {
                continue;
            }

            double stationLat =
                    station.get("latitude").asDouble();

            double stationLon =
                    station.get("longitude").asDouble();

            double distance = WeatherUtilities.distanceMiles(
                    latitude,
                    longitude,
                    stationLat,
                    stationLon
            );

            /*
             * The NOAA search uses a rectangular bounding box.
             * Enforce the requested circular radius here.
             */
            if (distance <= radiusMiles
                    && distance < closestDistance) {

                closestDistance = distance;
                closest = station;
            }
        }

        return closest != null
                ? closest.get("id").asText()
                : null;
    }


}