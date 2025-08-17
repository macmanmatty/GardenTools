package com.example.FruitTrees.OpenMeteo;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
/**
 * service class for making Open-Meteo requests
 */
@Service
public class OpenMeteoHTTPRequest {
    @Value("${open-meteo.url}")
   private  String openMeteoUrl;

   private final  RestTemplate restTemplate;
    private final  CacheManager cacheManager;
    @Autowired
    public OpenMeteoHTTPRequest(CacheManager cacheManager, RestTemplate restTemplate) {
        this.cacheManager = cacheManager;
        this.restTemplate = restTemplate;
    }
    
    /**
     * calls the open-meteo service to get the data for specified location(s)
     * in the weather request one open-meteo request is required per location
     * @param weatherRequest the weather request object
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "openMeteoDataCache",
      key = "#location.getLatitude() + ':' + #location.getLongitude() + ':' + #weatherRequest.getHourlyDataTypes.hashCode() + ':' + #weatherRequest.getStartDate() + ':' + #weatherRequest.getEndDate()")
    public OpenMeteoLocationResponse makeLocationRequest(Location location, WeatherRequest weatherRequest){
        String fullUrl = openMeteoUrl + "?latitude=" + location.getLatitude() +
                "&longitude=" + location.getLongitude() +
                "&start_date=" + weatherRequest.getStartDate() +
                "&end_date=" + weatherRequest.getEndDate();
        Set<String> hourlyDataTypes = weatherRequest.getHourlyDataTypes();
        for (String dataType : hourlyDataTypes) {
            fullUrl = fullUrl + "&hourly=" + DataUtilities.toOpenMeteoDatatype(dataType);
        }
        fullUrl = addConversionUnits(fullUrl, weatherRequest);
        Logger.getLogger("").info("getting weather data from open-meteo with  url " + fullUrl);
        ResponseEntity<OpenMeteoResponse> response = restTemplate.getForEntity(fullUrl, OpenMeteoResponse.class);
        Logger.getLogger("").info("obtained weather data from open-meteo ");

        OpenMeteoLocationResponse locationResponse=new OpenMeteoLocationResponse();
        locationResponse.setOpenMeteoResponse(response.getBody());
        locationResponse.setLocation(location);
            return locationResponse;
    }

    /**
     * calls the open-meteo service to get the data for specified location(s)
     * in the weather request one open-meteo request is required per location
     * @param weatherRequest the weather request object
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "openMeteoDataCache",
            key = "#location.getLatitude() + ':' + #location.getLongitude() + ':' + #weatherRequest.getHourlyDataTypes.hashCode() + ':' + #weatherRequest.getStartDate() + ':' + #weatherRequest.getEndDate()")
    public OpenMeteoLocationResponse makeLocationRequest(
            Location location,
            String startDate,
            String endDate,
            WeatherRequest weatherRequest
    ) {
        // Build a stable, comma-separated hourly param (sorted so cache keys are deterministic)
        List<String> hourly = weatherRequest.getHourlyDataTypes() == null
                ? List.of()
                : weatherRequest.getHourlyDataTypes().stream()
                .map(DataUtilities::toOpenMeteoDatatype)
                .distinct()
                .sorted()
                .toList();

        UriComponentsBuilder b = UriComponentsBuilder.fromHttpUrl(openMeteoUrl)
                .queryParam("latitude", location.getLatitude())
                .queryParam("longitude", location.getLongitude())
                .queryParam("start_date", startDate)
                .queryParam("end_date", endDate);

        if (!hourly.isEmpty()) {
            b.queryParam("hourly", String.join(",", hourly)); // single param
        }
        // Units (adapt this to how your addConversionUnits worked; shown inline here)
        if ("fahrenheit".equalsIgnoreCase(weatherRequest.getTemperatureUnit())) {
            b.queryParam("temperature_unit", "fahrenheit");
        }
        if ("inch".equalsIgnoreCase(weatherRequest.getPrecipitationUnit())) {
            b.queryParam("precipitation_unit", "inch");
        }

        URI uri = b.build(true).toUri();

        ResponseEntity<OpenMeteoResponse> response = restTemplate.getForEntity(uri, OpenMeteoResponse.class);
        OpenMeteoResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("Null Open-Meteo response for " + uri);
        }
        OpenMeteoLocationResponse locationResponse=new OpenMeteoLocationResponse();
        locationResponse.setOpenMeteoResponse(response.getBody());
        if (locationResponse.getLocation() == null) {
            locationResponse.setLocation(location);
        }
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
