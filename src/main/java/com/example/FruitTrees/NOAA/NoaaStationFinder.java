package com.example.FruitTrees.NOAA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;
@Component
public class NoaaStationFinder {

        @Value("${noaa.station.url}")
     private   String  noaaStationUrl;
    @Value("${noaa.api.key}")
    private  String noaaApiKey;
        private final RestTemplate restTemplate = new RestTemplate();
        private final ObjectMapper objectMapper = new ObjectMapper();

        /**
         * Find the nearest NOAA station to a given latitude and longitude
         */
        public String findNearestStation (double latitude,  double longitude, double radius,   String startDate, String endDate, String dataType) {

            try {
                String url = String.format(
                        "%s?datasetid=GHCND"
                                + "&extent=%s"
                                + "&datatypeid=%s"
                                + "&startdate=%s"
                                + "&enddate=%s"
                                + "&limit=1000",
                        noaaStationUrl,
                        getExtent(radius,latitude,longitude),
                        dataType,
                        startDate,
                        endDate
                );
                Logger.getLogger("").info("noaa url " + url);
                HttpHeaders headers = new HttpHeaders();
                headers.set("token", noaaApiKey);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                JsonNode json = objectMapper.readTree(response.getBody());
                JsonNode results = json.get("results");

                if (results != null && results.isArray() && !results.isEmpty()) {
                    return getClosestStation(results, latitude, longitude);
                }

            } catch (Exception e) {
                System.err.println("Error while fetching station ID: " + e.getMessage());
            }
            return null; // no station found
        }
    private String getExtent(double radius, double lat , double lon) {



        String extent = String.format(
                "%f,%f,%f,%f",
                lat - radius,
                lon - radius,
                lat + radius,
                lon + radius
        );

        return extent;
    }

    private String getClosestStation(
            JsonNode results,
            double latitude,
            double longitude) {

        JsonNode closest = null;
        double closestDistance = Double.MAX_VALUE;

        for (JsonNode station : results) {

            double stationLat = station.get("latitude").asDouble();
            double stationLon = station.get("longitude").asDouble();

            double distance = distanceMiles(
                    latitude,
                    longitude,
                    stationLat,
                    stationLon
            );

            if (distance < closestDistance) {
                closestDistance = distance;
                closest = station;
            }
        }

        return closest != null
                ? closest.get("id").asText()
                : null;
    }

    private double distanceMiles(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double earthRadiusMiles = 3958.8;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(Math.toRadians(lat1)) *
                                Math.cos(Math.toRadians(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return earthRadiusMiles * c;
    }
}
