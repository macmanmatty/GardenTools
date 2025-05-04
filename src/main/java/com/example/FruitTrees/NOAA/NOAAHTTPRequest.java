package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private NoaaStationFinder noaaStationFinder= new NoaaStationFinder();
    @Autowired
    public NOAAHTTPRequest(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
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
        int pageSize=100;
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

            TimeUnit.MILLISECONDS.sleep(rateLimit); // avoid rate limit
        }
       NOAAHourlyDataMap noaaHourlyDataMap= new NOAAHourlyDataMap();
        noaaHourlyDataMap.addRecords(allData);
        locationResponse.setNoaaHourlyDataMap(noaaHourlyDataMap);
        return locationResponse;
    }
    
    /**
     *  adds additional params to the url to specify the units
     * you wish to have data sent back in Fahrenheit, Celsius, Inches , Meters Etc.
     * @param openMeteoUrl the request url for open meteo
     * @param weatherRequest the weather request object
     * @return
     */
    public String addConversionUnits(String openMeteoUrl, WeatherRequest weatherRequest){
        String temperatureUnit=weatherRequest.getTemperatureUnit();
        String windSpeedUnit=weatherRequest.getWindSpeedUnit();
        String precipitationUnit=weatherRequest.getPrecipitationUnit();
        if(temperatureUnit!=null && !temperatureUnit.isEmpty()) {
         openMeteoUrl=openMeteoUrl+   "&temperature_unit=" + weatherRequest.getTemperatureUnit();
        }
            if(windSpeedUnit!=null && !windSpeedUnit.isEmpty()) {
              openMeteoUrl=openMeteoUrl+  "&wind_speed_unit=" + weatherRequest.getWindSpeedUnit();
            }
            if(precipitationUnit!=null && !precipitationUnit.isEmpty()) {
              openMeteoUrl=openMeteoUrl+  "&precipitation_unit=" + weatherRequest.getPrecipitationUnit();
            }
         return    openMeteoUrl;
  }
}
