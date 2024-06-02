package com.example.FruitTrees.WeatherProcessor;
import ch.qos.logback.classic.Logger;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;
import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;
import com.example.FruitTrees.OpenMeteo.OpenMeteoLocationResponses;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeatherProcessorService {
   private  ApplicationContext applicationContext;
    public WeatherProcessorService(@Autowired  ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    /**
     *  processes hourly weather data
     * @param weatherRequest the weather request object
     * @param openMeteoResponses the data from the open meteo API call for each location specified
     *  in the WeatherRequest Object
     * @return WeatherResponse object containing all of the processed data from the processors
     */
    public WeatherResponse processHourlyData( WeatherRequest weatherRequest, OpenMeteoLocationResponses openMeteoResponses){
        List<LocationResponse> locationResponses=openMeteoResponses.getLocationResponses();
        WeatherResponse weatherResponse = new WeatherResponse();
        for(LocationResponse locationResponse: locationResponses) {
          processLocationData(locationResponse, weatherRequest, weatherResponse);
        }
        return weatherResponse;
    }
    /**
     * processes the data for each individual location specified in the WeatherRequest Object
     * @param locationResponse the location response object holding the location object and
     *  open meteo data for that location
     * @param weatherRequest The WeatherRequest Object
     * @param weatherResponse  The WeatherResponse object to add all of  processed data from the processors to
     * @return WeatherResponse object containing all the processed data from the processors
     */
 private WeatherResponse   processLocationData(LocationResponse locationResponse, WeatherRequest weatherRequest,  WeatherResponse weatherResponse){
     Location location=locationResponse.getLocation();
    LocationWeatherResponse locationWeatherResponse= new LocationWeatherResponse();
     locationWeatherResponse.setLocation(location);
     String name = locationResponse.getLocation().getName()+" At: "+"Lat: " + locationResponse.getLocation().getLatitude()+" Lon: " +locationResponse.getLocation().getLongitude();
     weatherResponse.getLocationWeatherResponses().put(name,locationWeatherResponse);
     String text="Values For Location: "+location.getName();
     locationWeatherResponse.getLocationResponses().add(text);
     OpenMeteoResponse openMeteoResponse=locationResponse.getOpenMeteoResponse();
     List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = weatherRequest.getHourlyWeatherProcessRequests();
     List<String> time = openMeteoResponse.hourly.time;
     for (HourlyWeatherProcessRequest hourlyWeatherProcessRequest : hourlyWeatherProcessRequests) {
         try {
             WeatherProcessor weatherProcessor = applicationContext.getBean(hourlyWeatherProcessRequest.getProcessorName(), WeatherProcessor.class);
             processHourlyWeather( time, weatherProcessor,hourlyWeatherProcessRequest,  openMeteoResponse, locationWeatherResponse);
         }
         catch( BeanCreationException e){
             Lo
         }
     }
        return weatherResponse;
 }
    /**
     *
     * @param weatherProcessor
     * @param hourlyWeatherProcessRequest
     * @param openMeteoResponse
     * @param locationWeatherResponse
     * @param openMeteoDateAndTime
     */
public void processHourlyWeather( List<String> openMeteoDateAndTime,  WeatherProcessor weatherProcessor, HourlyWeatherProcessRequest hourlyWeatherProcessRequest,
                                 OpenMeteoResponse openMeteoResponse,
                                  LocationWeatherResponse locationWeatherResponse){
     weatherProcessor.setStartMonthDay(hourlyWeatherProcessRequest.getStartProcessMonth(), hourlyWeatherProcessRequest.getStartProcessDay());
     weatherProcessor.setEndMonthDay(hourlyWeatherProcessRequest.getEndProcessMonth(), hourlyWeatherProcessRequest.getEndProcessDay());
     weatherProcessor.setInputParameters(hourlyWeatherProcessRequest.getInputParameters());
     weatherProcessor.setDataType(hourlyWeatherProcessRequest.getHourlyDataType());
     weatherProcessor.setLocationWeatherResponse(locationWeatherResponse);
     weatherProcessor.setOnlyCalculateAverage(hourlyWeatherProcessRequest.isOnlyCalculateAverage() && hourlyWeatherProcessRequest.isCalculateAverage());
     List<? extends Number> data = DataUtilities.getHourlyData(openMeteoResponse, hourlyWeatherProcessRequest.getHourlyDataType());
     int size = data.size();
     weatherProcessor.before();
     for (int count = 0; count < size; count++) {
         weatherProcessor.processWeather(data.get(count), openMeteoDateAndTime.get(count));
     }
     weatherProcessor.after();
     if(hourlyWeatherProcessRequest.isCalculateAverage()){
         weatherProcessor.calculateAverage();
     }
     List<String> values = weatherProcessor.getValues();
     locationWeatherResponse.getLocationResponses().addAll(values);
 }

}
