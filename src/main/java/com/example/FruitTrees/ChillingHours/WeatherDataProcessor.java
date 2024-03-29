package com.example.FruitTrees.ChillingHours;
import com.example.FruitTrees.ChillingHours.WeatherProcessors.WeatherProcessor;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;
import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;
import com.example.FruitTrees.OpenMeteo.OpenMeteoLocationResponses;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class WeatherDataProcessor {
    /**
     * map of WeatherProcessor Components auto generated  by spring
     * key=component name
     * value= WeatherProcessor
     */
    Map<String, WeatherProcessor> weatherProcessorMap;
    public WeatherDataProcessor( @Autowired  Map<String, WeatherProcessor> weatherProcessorMap) {
        this.weatherProcessorMap = weatherProcessorMap;
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
     * @return WeatherResponse object containing all of the processed data from the processors
     */
 private WeatherResponse   processLocationData(LocationResponse locationResponse, WeatherRequest weatherRequest,  WeatherResponse weatherResponse){
     Location location=locationResponse.getLocation();
    LocationWeatherResponse locationWeatherResponse= new LocationWeatherResponse();
     locationWeatherResponse.setLocation(location);
     weatherResponse.getLocationWeatherResponses().put(locationResponse.getLocation().getName(),locationWeatherResponse);
     String text="Values For Location: "+location.getName();
     //weatherResponse.getResponses().add(text);
     locationWeatherResponse.getLocationResponses().add(text);
     OpenMeteoResponse openMeteoResponse=locationResponse.getOpenMeteoResponse();
     List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = weatherRequest.getHourlyWeatherProcessRequests();
     List<String> time = openMeteoResponse.hourly.time;

     for (HourlyWeatherProcessRequest hourlyWeatherProcessRequest : hourlyWeatherProcessRequests) {
         WeatherProcessor weatherProcessor = weatherProcessorMap.get(hourlyWeatherProcessRequest.getProcessorName());

         if(weatherProcessor==null){
             continue;
         }
         processHourlyWeather( time, weatherProcessor,hourlyWeatherProcessRequest,  openMeteoResponse, locationWeatherResponse);
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
 public void processHourlyWeather(List<String> openMeteoDateAndTime,  WeatherProcessor weatherProcessor, HourlyWeatherProcessRequest hourlyWeatherProcessRequest,
                                 OpenMeteoResponse openMeteoResponse,
                                  LocationWeatherResponse locationWeatherResponse){
     weatherProcessor.setStartMonthDay(hourlyWeatherProcessRequest.getStartProcessMonth(), hourlyWeatherProcessRequest.getStartProcessDay());
     weatherProcessor.setEndMonthDay(hourlyWeatherProcessRequest.getEndProcessMonth(), hourlyWeatherProcessRequest.getEndProcessDay());
     weatherProcessor.setInputParameters(hourlyWeatherProcessRequest.getInputParameters());
     weatherProcessor.setDataType(hourlyWeatherProcessRequest.getHourlyDataType());
     weatherProcessor.setLocationWeatherResponse(locationWeatherResponse);
     List<? extends Number> data = DataUtilities.getHourlyData(openMeteoResponse, hourlyWeatherProcessRequest.getHourlyDataType());
     int size = data.size();
     weatherProcessor.before();
     for (int count = 0; count < size; count++) {
         weatherProcessor.processWeather(data.get(count), openMeteoDateAndTime.get(count));
     }
     List<String> values = weatherProcessor.getValues();
     //weatherResponse.getResponses().addAll(values);
     //weatherResponse.getResponses().add("");
     locationWeatherResponse.getLocationResponses().addAll(values);


 }

 public int findDate(List<String> openMeteoDateAndTime, String startDateTime){
     String localDateTimeStart=DateUtilities.convertStringDateStringDateTime(startDateTime);
     int size=openMeteoDateAndTime.size();
     for(int count=0; count<size; count++){
         if(openMeteoDateAndTime.equals(localDateTimeStart)){
             return count;
         }

     }
     throw new IllegalArgumentException("Date: "+ startDateTime+ "Is  Out Of Bounds");

 }

}
