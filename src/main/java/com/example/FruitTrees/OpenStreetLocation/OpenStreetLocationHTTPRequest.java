package com.example.FruitTrees.OpenStreetLocation;

import com.example.FruitTrees.Location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * service class for making Open-Meteo requests
 */
@Service
public class OpenStreetLocationHTTPRequest {
    @Value("${open-street-location-url}")
   private  String openStreetUrl;
   private final  RestTemplate restTemplate = new RestTemplate();
    private final  CacheManager cacheManager;
    @Autowired
    public OpenStreetLocationHTTPRequest(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    /**
        calls the open street map service to get the data for  a specified location
     *  with given latitude and longitude
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "locationCache",
      key = "#location.getLatitude() + ':' + #location.getLongitude()")
    public OpenStreetLocationResponse makeOpenStreetLocationRequest(Location location){
        String fullUrl = String.format(openStreetUrl +"reverse?format=json&lat=%s&lon=%s", location.getLatitude(), location.getLongitude());
        Logger.getLogger("").info("open street url " + fullUrl);
        ResponseEntity<OpenStreetLocationResponse> response = restTemplate.getForEntity(fullUrl, OpenStreetLocationResponse.class);

            return response.getBody();
    }
    /**
     calls the open street map service to get the data for  a specified location
     *  with given state and county
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "locationCache",
            key = "#location.getCounty()")
    public OpenStreetLocationResponse makeOpenStreetLocationRequestByCountyAndState(Location location){

        String fullUrl =openStreetUrl +"search?format=json&q="+formatCounty(location.getCounty())+",+"+location.getState();
        Logger.getLogger("").info("open street url " + fullUrl);
        ResponseEntity<List<OpenStreetLocationResponse>> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OpenStreetLocationResponse>>() {}
        );
        return response.getBody().get(0);
    }
    private String formatCounty(String county){
        String [] names=county.split(" ");
        if(names.length>1) {
            return names[0] + "+" + names[1];
        }
        return names[0]+"+County";
    }


}
