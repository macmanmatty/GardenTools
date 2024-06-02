package com.example.FruitTrees.UsCensus;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.UsCensus.Request.USCensusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * service class for making Open-Meteo requests
 */
@Service
public class UsCensusHTTPRequest {
    @Value("${us-census-api-url}")
   private  String usCensusUrl;
    @Value("${us-census-api-key}")
    private  String usCensusKey;
   private final  RestTemplate restTemplate = new RestTemplate();
    private final  CacheManager cacheManager;
    @Autowired
    public UsCensusHTTPRequest(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    /**
        calls the us census service to get the data for  a specified location
     *  with given latitude and longitude
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "censusCache",
      key = "#usCensusRequest.getlocation().getState()")  public UsCensusResponse makeUsCensusRequest(USCensusRequest usCensusRequest){
        Location location=usCensusRequest.getLocation();
        String fullUrl = usCensusUrl +"/data/"+usCensusRequest.getYear()+"/acs/acs5?get=NAME";
                for(String variable:usCensusRequest.getDataFields()) {
                    fullUrl=fullUrl+","+variable;
                }
                fullUrl=fullUrl+" for=county:"+location.getCounty()+"&in = state:"+location.getStateFips()+"&key="+usCensusKey;
        ResponseEntity<UsCensusResponse> response = restTemplate.getForEntity(fullUrl, UsCensusResponse.class);
            return response.getBody();
    }

}
