package com.example.FruitTrees.Regrid;

import com.example.FruitTrees.Location.Coordinate;
import com.example.FruitTrees.Location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * service class for making Open-Meteo requests
 */
@Service
public class RegridHTTPRequest {
    @Value("${regrid-url}")
   private  String regridURL;
    @Value("${regrid-api-key}")
    private  String regrdiApiKey;

    private final  RestTemplate restTemplate = new RestTemplate();
    private final  CacheManager cacheManager;
    @Autowired
    public RegridHTTPRequest(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    /**
        calls the regrid map service to get the property bounds  data for  a specified location
     *  with given latitude and longitude
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "locationCache", key = "#location.getLatitude() + ':' + #location.getLongitude()")
    public Location makeRegridLocationRequest(Location location) {
        String apiToken = "YOUR_API_TOKEN"; // Replace with your actual token or inject it securely
        String fullUrl = String.format(
                "%s/api/v2/us/parcels/point?lat=%s&lon=%s&token=%s",
                regridURL, location.getLatitude(), location.getLongitude(), apiToken
        );

        Logger.getLogger("").info("Regrid API URL: " + fullUrl);

        try {
            ResponseEntity<RegridResponse> response = restTemplate.getForEntity(fullUrl, RegridResponse.class);
            RegridResponse regridData = response.getBody();
            if (regridData != null && regridData.getFeatures() != null && !regridData.getFeatures().isEmpty()) {
                // Extract coordinates
                List<List<Double>> coordinatesList = regridData.getFeatures().get(0).getGeometry().getCoordinates().get(0);
                List<Coordinate> coords = new ArrayList<>();
                for (List<Double> coordPair : coordinatesList) {
                    if (coordPair.size() == 2) {
                        Coordinate coord = new Coordinate(coordPair.get(1), coordPair.get(0)); // lat, lon
                        coords.add(coord);
                    }
                }

                location.getLocationArea().setCoordinates(coords);
            }

        } catch (Exception e) {
            Logger.getLogger("").severe("Failed to fetch Regrid data: " + e.getMessage());
        }

        return location;
    }

}
