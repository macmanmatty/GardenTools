package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.google.common.util.concurrent.RateLimiter;
/**
 * service class for making Open-Meteo requests
 */
@Service
public class NOAAHTTPRequest {
    @Value("${noaa.url}")
   private  String noaaUrl;
    @Value("${noaa.api.key}")
    private  String noaaApiKey;
    @Value("${noaa.api.rate-limit}")
    private  int rateLimit;
    private final  RestTemplate restTemplate = new RestTemplate();
    private final  CacheManager cacheManager;
    private NoaaStationFinder noaaStationFinder;
    private final RateLimiter limiter = RateLimiter.create(2.0); // 2 permits per second
    @Autowired
    public NOAAHTTPRequest(CacheManager cacheManager,  NoaaStationFinder noaaStationFinder) {
        this.cacheManager = cacheManager;
        this.noaaStationFinder = noaaStationFinder;
    }
    
    /**
     * calls the noaa service to get the data for specified location(s)
     * in the weather request one noaa arequest is required per location
     * @param weatherRequest the weather request object
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "openMeteoDataCache",
     key = "#location.getLatitude() + ':' + #location.getLongitude() + ':' + #weatherRequest.getHourlyDataTypes.hashCode() + ':' + #weatherRequest.getStartDate() + ':' + #weatherRequest.getEndDate()")
    public NOAALocationResponse makeLocationRequest (Location location, WeatherRequest weatherRequest) throws InterruptedException {
       NOAALocationResponse locationResponse = new NOAALocationResponse();
        NOAAResponse noaaResponse = new NOAAResponse();
        List<NOAAWeatherRecord> allData = new ArrayList<>();
        int offset = 1;
        boolean moreData = true;
        int pageSize=1000;
        String stationId= location.getStationId();
        if(stationId==null || stationId.isEmpty()){
            stationId=noaaStationFinder.findNearestStation(location.getLatitude(), location.getLongitude());
        }

        while (moreData) {
            String url = noaaUrl+ "?datasetid=global-hourly"
                    + "&stationid=" + stationId
                    + "&startdate=" + weatherRequest.getStartDate()
                    + "&enddate=" + weatherRequest.getEndDate()
                    + "&units=standard"
                    + "&limit=" + pageSize
                    + "&offset=" + offset;
          Set<String> dataTypes=weatherRequest.getHourlyDataTypes();
            for (String datatype : dataTypes) {
                url += "&datatypeid=" + datatype;

            }
            Logger.getLogger("").info("noaa url " + url);
            Logger.getLogger("").info("noaa station id " + stationId);

            HttpHeaders headers = new HttpHeaders();
            headers.set("token", noaaApiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<NOAAResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    NOAAResponse.class
            );

            List<NOAAWeatherRecord> results = Objects.requireNonNull(response.getBody()).getNoaaHourlyObservations();
            if (results == null || results.isEmpty()) {
                moreData = false;
            } else {
                allData.addAll(results);
                if (results.size() < pageSize) {
                    moreData = false;
                } else {
                    offset += pageSize;
                }
            }
            limiter.acquire(); // This blocks until a permit is available

        }
       NOAAHourlyDataMap noaaHourlyDataMap= new NOAAHourlyDataMap();
        noaaHourlyDataMap.addRecords(allData);
        locationResponse.setNoaaHourlyDataMap(noaaHourlyDataMap);
        return locationResponse;
    }
    

}
