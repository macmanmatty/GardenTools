package com.example.FruitTrees.OpenMeteo;
import com.example.FruitTrees.ChillingHours.WeatherDataProcessor;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.WeatherConroller.BadRequestException;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
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
   private final  RestTemplate restTemplate = new RestTemplate();
    private final  CacheManager cacheManager;
    @Autowired
    public OpenMeteoHTTPRequest(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    /**
     * calls the open-meteo service to get the data for specified location(s)
     * in the weather request one open-meteo request is required per location
     * @param weatherRequest the weather request object
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "weatherDataCache",
      key = "#location.getLatitude() + ':' + #location.getLongitude() + ':' + #weatherRequest.getHourlyDataTypes.hashCode() + ':' + #weatherRequest.getStartDate() + ':' + #weatherRequest.getEndDate()")
    public LocationResponse makeLocationRequest( Location location, WeatherRequest weatherRequest){
        String fullUrl = openMeteoUrl + "?latitude=" + location.getLatitude() +
                "&longitude=" + location.getLongitude() +
                "&start_date=" + weatherRequest.getStartDate() +
                "&end_date=" + weatherRequest.getEndDate();
        Set<String> hourlyDataTypes = weatherRequest.getHourlyDataTypes();
        for (String dataType : hourlyDataTypes) {
            fullUrl = fullUrl + "&hourly=" + dataType;
        }
        fullUrl = addConversionUnits(fullUrl, weatherRequest);
        Logger.getLogger("").info("full url " + fullUrl);
        ResponseEntity<OpenMeteoResponse> response = restTemplate.getForEntity(fullUrl, OpenMeteoResponse.class);
        LocationResponse locationResponse=new LocationResponse();
        locationResponse.setOpenMeteoResponse(response.getBody());
        locationResponse.setLocation(location);
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
              openMeteoUrl=openMeteoUrl+  "&precipitation_unit" + weatherRequest.getPrecipitationUnit();
            }
         return    openMeteoUrl;
  }
}
