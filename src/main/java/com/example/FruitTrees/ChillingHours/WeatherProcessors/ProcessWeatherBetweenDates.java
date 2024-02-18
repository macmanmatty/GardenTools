package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import com.example.FruitTrees.ChillingHours.WeatherDataProcessor;

import java.time.LocalDateTime;

public abstract  class ProcessWeatherBetweenDates  extends WeatherProcessor {
    private boolean counting;
    public ProcessWeatherBetweenDates(String name) {
        super(name);
    }

    @Override
    public void before() {
        values.clear();

    }

    @Override
    public final  void processWeather(Number value, String date) {
            if(isStartDate(date)){
                counting=true;
                onStartDate(date);
            }

            if(counting){
                processWeatherBetween(value, date);
              
            }
            if(isEndDate(date) && counting){
                counting=false;
                onEndDate(date);
            }
        }

    protected abstract void onStartDate(String date);


    protected abstract void onEndDate(String date);

    abstract void processWeatherBetween(Number data, String date);
    
}
    

