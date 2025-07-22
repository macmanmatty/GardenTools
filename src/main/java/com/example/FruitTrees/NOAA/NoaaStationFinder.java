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
        public String findNearestStation (String latitude,  String longitude,  String startDate, String endDate, String dataType) {

            try {
                String url = String.format(
                        "%s?limit=1&latitude=%s&longitude=%s&datatypeid=%s&startdate=%s&enddate=%s",
                        noaaStationUrl, latitude, longitude, dataType, startDate, endDate
                );
                Logger.getLogger("").info("noaa url " + url);
                HttpHeaders headers = new HttpHeaders();
                headers.set("token", noaaApiKey);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                JsonNode json = objectMapper.readTree(response.getBody());
                JsonNode results = json.get("results");
                if (results != null && results.isArray() && !results.isEmpty()) {
                    JsonNode station = results.get(0);
                    return station.get("id").asText();  // Example: GHCND:USW00012921
                }
            } catch (Exception e) {
                System.err.println("Error while fetching station ID: " + e.getMessage());
            }
            return null; // no station found
        }



}
