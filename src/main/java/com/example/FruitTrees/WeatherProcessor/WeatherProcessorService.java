package com.example.FruitTrees.WeatherProcessor;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import com.example.FruitTrees.OpenMeteo.LocationResponses;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeatherProcessorService {
    private static final Logger log = LoggerFactory.getLogger(WeatherProcessorService.class);
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
    public WeatherResponse processHourlyData( WeatherRequest weatherRequest, LocationResponses openMeteoResponses){
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
 private WeatherResponse   processLocationData(LocationResponse locationResponse, WeatherRequest weatherRequest, WeatherResponse weatherResponse){
     Location location =locationResponse.getLocation();
    LocationWeatherResponse locationWeatherResponse= new LocationWeatherResponse();
     locationWeatherResponse.setLocation(location);
     String name = locationResponse.getLocation().getName()+" At: "+"Lat: " + locationResponse.getLocation().getLatitude()+" Lon: " +locationResponse.getLocation().getLongitude();
     weatherResponse.getLocationWeatherResponses().put(name,locationWeatherResponse);
     String text="Values For Location: "+ location.getName();
     locationWeatherResponse.getLocationResponses().add(text);
     List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = weatherRequest.getHourlyWeatherProcessRequests();
     List<String> time = locationResponse.getTime();
     for (HourlyWeatherProcessRequest hourlyWeatherProcessRequest : hourlyWeatherProcessRequests) {
             WeatherProcessor weatherProcessor = applicationContext.getBean(hourlyWeatherProcessRequest.getProcessorName(), WeatherProcessor.class);
             processHourlyWeather( time, weatherProcessor,hourlyWeatherProcessRequest,  locationResponse, locationWeatherResponse);
     }
        return weatherResponse;
 }
    /**
     *
     * @param weatherProcessor
     * @param hourlyWeatherProcessRequest
     * @param locationWeatherResponse
     * @param openMeteoDateAndTime
     */
public void processHourlyWeather( List<String> openMeteoDateAndTime,  WeatherProcessor weatherProcessor, HourlyWeatherProcessRequest hourlyWeatherProcessRequest,
                                 LocationResponse locationResponse,
                                  LocationWeatherResponse locationWeatherResponse){
     weatherProcessor.setStartMonthDay(hourlyWeatherProcessRequest.getStartProcessMonth(), hourlyWeatherProcessRequest.getStartProcessDay());
     weatherProcessor.setEndMonthDay(hourlyWeatherProcessRequest.getEndProcessMonth(), hourlyWeatherProcessRequest.getEndProcessDay());
     weatherProcessor.setInputParameters(hourlyWeatherProcessRequest.getInputParameters());
     weatherProcessor.setDataType(hourlyWeatherProcessRequest.getHourlyDataType());
     weatherProcessor.setLocationWeatherResponse(locationWeatherResponse);
     weatherProcessor.setOnlyCalculateAverage(hourlyWeatherProcessRequest.isOnlyCalculateAverage() && hourlyWeatherProcessRequest.isCalculateAverage());
     List<? extends Number> data = locationResponse.getData(hourlyWeatherProcessRequest.getHourlyDataType());
     int size = data.size();
     weatherProcessor.before();
    log.info(" started processing of {}", weatherProcessor.getProcessorName() +" for "+locationResponse.getLocation().getName());

    for (int count = 0; count < size; count++) {
         weatherProcessor.processWeatherExternal(data.get(count), openMeteoDateAndTime.get(count));
     }
     weatherProcessor.after();
     if(hourlyWeatherProcessRequest.isCalculateAverage()){
         weatherProcessor.calculateAverage();
     }
     List<String> values = weatherProcessor.getProcessedTextValues();
     locationWeatherResponse.getLocationResponses().addAll(values);
 }


}
