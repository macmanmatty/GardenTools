package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.ChillingHours.WeatherDataProcessor;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.WeatherConroller.BadRequestException;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class OpenMeteoService {
    @Value("${open-meteo.url}")
   private  String openMeteoUrl;
    @Value("${enable.Multiple.Location.Processing}")
    private  boolean processMultipleLocationRequestsEnabled;
    @Value("${max.Multiple.OpenMeteo.Requests}")
    private  int maxMultipleOpenMeteoRequests;
   private  RestTemplate restTemplate = new RestTemplate();

   private WeatherDataProcessor weatherDataProcessor;


    public OpenMeteoService( @Autowired  WeatherDataProcessor weatherDataProcessor) {
        this.weatherDataProcessor = weatherDataProcessor;
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

    private OpenMeteoLocationResponses makeRequest(WeatherRequest weatherRequest) throws IOException {
      OpenMeteoLocationResponses openMeteoResponses=  new OpenMeteoLocationResponses();
      List<Location> locations=weatherRequest.getLocations();
      locationCheck(locations);
      for (Location location: locations) {
          try {
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
               openMeteoResponses.getLocationResponses().add(locationResponse);
          } catch (RestClientException e) {
              throw new IOException(e);
          }
      }

      return  openMeteoResponses;
    }

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
