package com.example.FruitTrees.ChillingHours;

import com.example.FruitTrees.ChillingHours.WeatherProcessors.WeatherProcessor;
import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;
import com.example.FruitTrees.OpenMeteo.HourlyWeatherProcessRequest;
import com.example.FruitTrees.OpenMeteo.WeatherRequest;
import com.example.FruitTrees.OpenMeteo.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class WeatherDataProcessor {

    Map<String, WeatherProcessor> weatherProcessorMap;

    public WeatherDataProcessor( @Autowired  Map<String, WeatherProcessor> weatherProcessorMap) {
        this.weatherProcessorMap = weatherProcessorMap;
    }

    public void calculateChillingHours(WeatherResponse chillingHoursResponse , WeatherRequest chillingHoursRequest, OpenMeteoResponse openMeteoResponse){
        Map< String,   Double> chillingHours=new HashMap<>();
        List<ChillingCalculationMethod>  chillingCalculationMethods=new ArrayList<>();
        for(ChillingCalculationMethod chillingCalculationMethod: chillingCalculationMethods) {
            double maxChillingTemp=chillingCalculationMethod.getMaxChillingTemp();
            double minChillingTemp=chillingCalculationMethod.getMinChillingTemp();
            List<Double> doubles = openMeteoResponse.hourly.temperature_2m;
            double chillHours=0;
            for (double temp : doubles) {
             if (temp<=maxChillingTemp && temp>=minChillingTemp){
                   chillHours++;
               }
            }
            chillingHours.put(chillingCalculationMethod.getName(), chillHours);
        }

    }
    public WeatherResponse processHourlyData( WeatherRequest weatherRequest, OpenMeteoResponse openMeteoResponse){
        WeatherResponse weatherResponse= new WeatherResponse();
        List<String> time=openMeteoResponse.hourly.time;
        List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests =weatherRequest.getHourlyWeatherProcessRequests();
        for(HourlyWeatherProcessRequest hourlyWeatherProcessRequest : hourlyWeatherProcessRequests){
            WeatherProcessor weatherProcessor=weatherProcessorMap.get(hourlyWeatherProcessRequest.getProcessorName());
            weatherProcessor.setStartMonthDay(hourlyWeatherProcessRequest.getStartProcessMonth(), hourlyWeatherProcessRequest.getStartProcessDay());
            weatherProcessor.setEndMonthDay(hourlyWeatherProcessRequest.getEndProcessMonth(), hourlyWeatherProcessRequest.getEndProcessDay());
            weatherProcessor.setInputParameters(hourlyWeatherProcessRequest.getInputParameters());
            List<? extends Number> data=DataUtilities.getHourlyData(openMeteoResponse, hourlyWeatherProcessRequest.getHourlyDataType());
            int size=data.size();
            weatherProcessor.before();
            for(int count=0; count<size; count++){
                weatherProcessor.processWeather(data.get(count), time.get(count));
            }
            List<String> values=weatherProcessor.getValues();
           weatherResponse.getResponses().addAll(values);
        }

        return weatherResponse;

    }

}
