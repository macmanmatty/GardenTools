package com.example.FruitTrees.ChillingHours;

import com.example.FruitTrees.ChillingHours.WeatherProcessors.MaxCalculator;
import com.example.FruitTrees.ChillingHours.WeatherProcessors.WeatherProcessor;
import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;
import com.example.FruitTrees.OpenMeteo.WeatherProcessRequest;
import com.example.FruitTrees.OpenMeteo.WeatherRequest;
import com.example.FruitTrees.OpenMeteo.WeatherResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherDataProcessor {

    Map<String, WeatherProcessor> weatherProcessorMap=new HashMap<>();
    Map<String, List<? extends Number>> dataSetsMap=new HashMap<>();

    public WeatherDataProcessor() {
        weatherProcessorMap.put("max", new MaxCalculator());
        weatherProcessorMap.put()
    }

    public void calculateChillingHours(WeatherResponse chillingHoursResponse , WeatherRequest chillingHoursRequest, OpenMeteoResponse openMeteoResponse){
        Map< String,   Double> chillingHours=new HashMap<>();
        List<ChillingCalculationMethod>  chillingCalculationMethods=new ArrayList<>();
        for(ChillingCalculationMethod chillingCalculationMethod: chillingCalculationMethods) {
            double maxChillingTemp=chillingCalculationMethod.getMaxChillingTemp();
            double minChillingTemp=chillingCalculationMethod.getMinChillingTemp();
            List<Double> doubles = openMeteoResponse.getHourly().getTemperature2m();
            double chillHours=0;
            for (double temp : doubles) {
                if (temp<=maxChillingTemp && temp>=minChillingTemp){
                    chillHours++;
                }
            }
            chillingHours.put(chillingCalculationMethod.getName(), chillHours);
        }

    }
    public WeatherResponse processData(WeatherResponse chillingHoursResponse , WeatherRequest weatherRequest, OpenMeteoResponse openMeteoResponse){
        WeatherResponse weatherResponse= new WeatherResponse();
        List<String> time=openMeteoResponse.getHourly().getTime();
        List<WeatherProcessRequest> weatherProcessRequests=weatherRequest.getWeatherProcessRequests();

        for(WeatherProcessRequest weatherProcessRequest: weatherProcessRequests){
            WeatherProcessor weatherProcessor=weatherProcessorMap.get(weatherProcessRequest.getProcessorName());
            weatherProcessor.setStartMonthDay(weatherProcessRequest.getStartProcessMonth(), weatherProcessRequest.getStartProcessDay());
            weatherProcessor.setEndMonthDay(weatherProcessor.getEndMonth(), weatherProcessor.getEndDay());
            List<? extends Number> data=DataUtilities.getHourlyData(openMeteoResponse, weatherProcessRequest.getHourlyDataType());
            int size=data.size();
            for(int count=0; count<size; count++){
                weatherProcessor.processWeather(data.get(count), time.get(count));
            }
            List<String> values=weatherProcessor.getValues();
           weatherResponse.getResponses().addAll(values);
        }

        int size=time.size();
        for(int count=0; count<size ; count++) {


        }
        return chillingHoursResponse;

    }


    public String getDate(String dateAndTime){
        String [] dates=dateAndTime.split("T");
        return dates[0];
    }

}
