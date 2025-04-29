package com.example.FruitTrees.OpenStreetMap;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenStreetMap.Model.OpenStreetLocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * service class for making Open-Meteo requests
 */
@Service
public class OpenStreetLocationHTTPRequest {
    @Value("${open-street-location-url}")
   private  String openStreetUrl;
    @Value("${app-name}")
    private String appName;
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

        String fullUrl =openStreetUrl +"search?format=json&q="+formatCounty(location.getCounty())+",+"+ location.getState();
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
    /**
     calls the open street map service to get the data for  a specified laddress
     *  with given latitude and longitude
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "locationCache",
            key = "#location.getStreetName() + ':' + #location.getStreetNumber()+':' + #location.getCity()+':' + #location.getStateAbbreviation() ")
    public Location forwardGeocodeAddressFromLocationObject(Location location){
        String fullUrl=buildGeocodeUrl(location);
        Logger.getLogger("").info("open street url " + fullUrl);
        ResponseEntity< OpenStreetLocationResponse []> response = restTemplate.getForEntity(fullUrl, OpenStreetLocationResponse[].class);
        if(response.getBody().length>0){
            OpenStreetLocationResponse  res = response.getBody()[0];
            location.setLatitude(res.getLat());
            location.setLongitude(res.getLon());
        }

        return location;
    }

    /**
     calls the open street map service to get the data for  a specified laddress
     *  with given latitude and longitude
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "locationCache",
            key = "#address")
    public OpenStreetLocationResponse forwardGeocodeAddress(String address){

        String fullUrl = "https://nominatim.openstreetmap.org/search"
                + "?q=" + address.replace(" ", "+")
                + "&format=json&addressdetails=1";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", appName); // REQUIRED by Nominatim
        HttpEntity<String> entity = new HttpEntity<>(headers);
        Logger.getLogger("").info("app name " + appName);

        Logger.getLogger("").info("open street url " + fullUrl);
        ResponseEntity< OpenStreetLocationResponse []> response = restTemplate.exchange(fullUrl, HttpMethod.GET,entity, OpenStreetLocationResponse[].class);
        System.out.println("HTTP Status Code: " + response.getStatusCode());
        System.out.println("Response Headers: " + response.getHeaders());
        System.out.println("Response Body: " + Arrays.toString(response.getBody()));

        if(Objects.requireNonNull(response.getBody()).length>0) {
            return Objects.requireNonNull(response.getBody())[0];
        }
        return null;
    }


    /**
     * builds a open street map geocode url based off of an address location object
     * @param location the location object
     * @return the open street map  geocode url
     */
    public String buildGeocodeUrl(Location location) {
        String openStreetUrl = "https://nominatim.openstreetmap.org/";

        String fullAddress = String.format("%s %s, %s, %s %s",
                location.getStreetNumber(),
                location.getStreetName(),
                location.getCity(),
                location.getStateAbbreviation(),
                location.getZipCode());
        String encodedAddress = URLEncoder.encode(fullAddress, StandardCharsets.UTF_8);
        return String.format("%ssearch?q=%s&format=json", openStreetUrl, encodedAddress);
    }


}
