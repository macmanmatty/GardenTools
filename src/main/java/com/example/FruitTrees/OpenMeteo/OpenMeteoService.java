package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.ChillingHours.CalculationMode;
import com.example.FruitTrees.ChillingHours.ChillingHoursCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class OpenMeteoService {
    @Value("open-meteo.apikey")
   private  String apiKey;

    @Value("open-meteo.url")
   private  String openMeteoUrl;

   private  RestTemplate restTemplate = new RestTemplate();

   private ChillingHoursCalculator chillingHoursCalculator;

    public OpenMeteoService() {
        chillingHoursCalculator=new ChillingHoursCalculator();

    }

    public WeatherResponse getData(WeatherRequest weatherRequest ) throws IOException {
        WeatherResponse chillingHoursResponse= new WeatherResponse();
        OpenMeteoResponse openMeteoResponse=makeRequest(weatherRequest);
        chillingHoursResponse= chillingHoursCalculator.calculateYearlyWeather(chillingHoursResponse, weatherRequest, openMeteoResponse);
        return  chillingHoursResponse;
    }

    private OpenMeteoResponse makeRequest(WeatherRequest chillingHoursRequest) throws IOException {
        String fullUrl="?latitude="+chillingHoursRequest.getLatitude()+
                "&longitude="+chillingHoursRequest.getLongitude()+
                "&start_date="+chillingHoursRequest.getStartDate()+
                "&end_date="+chillingHoursRequest.getEndDate();
                List<String> hourlyDataTypes=chillingHoursRequest.getHourlyDataTypes();
                for(String dataType: hourlyDataTypes) {
                   fullUrl=fullUrl+ "&hourly=" + dataType;
                }
               ResponseEntity<OpenMeteoResponse> response=restTemplate.getForEntity(fullUrl, OpenMeteoResponse.class);
               if(response.getStatusCode()== HttpStatusCode.valueOf(200)) {
                   return response.getBody();
               }
               throw new IOException("Bad Response From Open Meteo  With following  Request: " +chillingHoursRequest.toString());
    }


}
