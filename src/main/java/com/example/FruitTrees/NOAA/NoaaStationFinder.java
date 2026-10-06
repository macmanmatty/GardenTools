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
     * Finds the nearest NOAA GHCN-Daily station within the specified
     * search radius that supports the requested data type and date range.
     *
     * @param latitude    latitude of the target location
     * @param longitude   longitude of the target location
     * @param radiusMiles search radius in miles
     * @param startDate   beginning of requested date range (yyyy-MM-dd)
     * @param endDate     end of requested date range (yyyy-MM-dd)
     * @param dataType    NOAA data type, such as TMAX or TMIN
     * @return NOAA station ID, or null if no station is found
     */
    public String findNearestStation(
            double latitude,
            double longitude,
            double radiusMiles,
            String startDate,
            String endDate,
            String dataType) {

        try {
            String url = String.format(
                    "%s?datasetid=GHCND"
                            + "&extent=%s"
                            + "&datatypeid=%s"
                            + "&startdate=%s"
                            + "&enddate=%s"
                            + "&limit=1000",
                    noaaStationUrl,
                    getExtent(radiusMiles, latitude, longitude),
                    dataType,
                    startDate,
                    endDate
            );

            Logger.getLogger("").info("NOAA station URL: " + url);

            // NOAA CDO API expects the API token in the "token" header.
            HttpHeaders headers = new HttpHeaders();
            headers.set("token", noaaApiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode results = json.get("results");

            if (results != null && results.isArray() && !results.isEmpty()) {
                return getClosestStation(
                        results,
                        latitude,
                        longitude,
                        radiusMiles
                );
            }

        } catch (Exception e) {
            Logger.getLogger("")
                    .severe("Error while fetching NOAA station ID: "
                            + e.getMessage());
        }

        return null;
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