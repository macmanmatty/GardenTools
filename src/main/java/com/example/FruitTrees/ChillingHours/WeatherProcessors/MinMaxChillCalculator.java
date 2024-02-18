package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component("MinMaxChill")

public class MinMaxChillCalculator  extends ProcessWeatherBetweenDates {
    private double chillHours;
    private double minTemp;
    private double maxTemp;
    public MinMaxChillCalculator() {
        super("Chill Hours");
    }

    @Override
    public void before() {
        if(inputParameters.size()<2){
            throw new IllegalArgumentException("Missing min and max parameters");
        }
        Double minTemp= Double.valueOf( inputParameters.get(0));
        this.minTemp=minTemp;
         Double maxTemp= Double.valueOf( inputParameters.get(1));
         this.maxTemp=maxTemp;
        values.clear();
    }



    @Override
    protected void onStartDate(String date) {

    }

    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        addValue(chillHours, localDateTime.getYear());
        chillHours =0;
    }

    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>=minTemp && value<=maxTemp) {
            chillHours++;
        }

    }

}
