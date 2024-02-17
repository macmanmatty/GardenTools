package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.ChillingHours.WeatherDataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
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

   private  RestTemplate restTemplate = new RestTemplate();

   private WeatherDataProcessor weatherDataProcessor;


    public OpenMeteoService( @Autowired  WeatherDataProcessor weatherDataProcessor) {
        this.weatherDataProcessor = weatherDataProcessor;
    }

    public WeatherResponse getData(WeatherRequest weatherRequest ) throws IOException {
        extractAdditionalDataTypes(weatherRequest);
        OpenMeteoResponse openMeteoResponse=makeRequest(weatherRequest);
      WeatherResponse  response= weatherDataProcessor.processHourlyData( weatherRequest, openMeteoResponse);
        return  response;
    }

    private void extractAdditionalDataTypes(WeatherRequest weatherRequest) {
        List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests=weatherRequest.hourlyWeatherProcessRequests;
        for(HourlyWeatherProcessRequest hourlyWeatherProcessRequest:hourlyWeatherProcessRequests){
            weatherRequest.getHourlyDataTypes().add(hourlyWeatherProcessRequest.getHourlyDataType());
        }
    }

    private OpenMeteoResponse makeRequest(WeatherRequest weatherRequest) throws IOException {
        try {
            String fullUrl = openMeteoUrl + "?latitude=" + weatherRequest.getLatitude() +
                    "&longitude=" + weatherRequest.getLongitude() +
                    "&start_date=" + weatherRequest.getStartDate() +
                    "&end_date=" + weatherRequest.getEndDate();
            Set<String> hourlyDataTypes = weatherRequest.getHourlyDataTypes();
            for (String dataType : hourlyDataTypes) {
                fullUrl = fullUrl + "&hourly=" + dataType;
            }
            fullUrl = addConversionUnits(fullUrl, weatherRequest);
            Logger.getLogger("").info("full url " + fullUrl);

            ResponseEntity<OpenMeteoResponse> response = restTemplate.getForEntity(fullUrl, OpenMeteoResponse.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new IOException(e);
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
