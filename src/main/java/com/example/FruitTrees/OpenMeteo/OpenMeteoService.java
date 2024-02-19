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
public class OpenMeteoService {
    @Value("${open-meteo.url}")
   private  String openMeteoUrl;
    @Value("${enable.Multiple.Location.Processing}")
    private  boolean processMultipleLocationRequestsEnabled;
    @Value("${max.Multiple.OpenMeteo.Requests}")
    private  int maxMultipleOpenMeteoRequests;
   private final  RestTemplate restTemplate = new RestTemplate();
   private  final WeatherDataProcessor weatherDataProcessor;

    private final  CacheManager cacheManager;

    public OpenMeteoService( @Autowired  WeatherDataProcessor weatherDataProcessor,  @Autowired  CacheManager cacheManager) {
        this.weatherDataProcessor = weatherDataProcessor;
        this.cacheManager=cacheManager;
    }
    public WeatherResponse getData(WeatherRequest weatherRequest ) throws IOException {
        extractAdditionalDataTypes(weatherRequest);
        OpenMeteoLocationResponses openMeteoResponses=makeRequest(weatherRequest);
      WeatherResponse  response= weatherDataProcessor.processHourlyData( weatherRequest, openMeteoResponses);
        return  response;
    }
    private void extractAdditionalDataTypes(WeatherRequest weatherRequest) {
        List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests=weatherRequest.hourlyWeatherProcessRequests;
        for(HourlyWeatherProcessRequest hourlyWeatherProcessRequest:hourlyWeatherProcessRequests){
            weatherRequest.getHourlyDataTypes().add(hourlyWeatherProcessRequest.getHourlyDataType());
        }
    }

    /**
     * calls the open-meteo service to get the data for  each of specified location(s)
     * in the weather request one open-meteo request is required per location
     * @param weatherRequest the weather request objcet
     * @return
     * @throws IOException
     */
    private OpenMeteoLocationResponses makeRequest(WeatherRequest weatherRequest) throws IOException {
      OpenMeteoLocationResponses openMeteoResponses=  new OpenMeteoLocationResponses();
      List<Location> locations=weatherRequest.getLocations();
      locationCheck(locations);
      for (Location location: locations) {
          try {
             LocationResponse locationResponse=makeLocationRequest(location, weatherRequest);
               openMeteoResponses.getLocationResponses().add(locationResponse);
          } catch (RestClientException e) {
              throw new IOException(e);
          }
      }
      return  openMeteoResponses;
    }

    /**
     * calls the open-meteo service to get the data for specified location(s)
     * in the weather request one open-meteo request is required per location
     * @param weatherRequest the weather request object
     * @return The LocationResponse containing the  OpenMeteoResponse and the Location Object
     * @throws IOException
     */
    @Cacheable(value = "weatherDataCache", key = "#location.latitude + ':' " +
            "+ #location.longitude + ':' + #weatherRequest.hourlyDataTypes + ':'" +
            " + #weatherRequest.startDate + ':' " +
            "+ #weatherRequest.endDate")
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
     * checks to make sure the number of locations
     * you are attempting to retrieve  data for is allowed
     * @param locations the list of locations
     */
    private void locationCheck(List<Location> locations) {
        int size=locations.size();
        if (size==0){
            throw new BadRequestException("No Locations Specified");
        }
        if(size>1 && !processMultipleLocationRequestsEnabled){
            throw new BadRequestException("You may only process one location at time currently");
        }
        else if(size>maxMultipleOpenMeteoRequests){
            throw new BadRequestException("You may only process "+maxMultipleOpenMeteoRequests+ " locations at time currently");
        }
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
