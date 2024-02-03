package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component("MinMaxChill")

public class MinMaxChillCalculator  extends WeatherProcessor {
    private double chillHours;

    private boolean counting;
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
    Double minTemp= inputParameters.get(0);
    if(minTemp!=null){
        this.minTemp=minTemp;
    }
    Double maxTemp= inputParameters.get(1);
        if(minTemp!=null){
            this.maxTemp=maxTemp;
        }

    }

    @Override
    public void processWeather(Number number, String date) {
        double value=number.doubleValue();
        boolean inDate=dateInRange(date);
        if(inDate){
            counting=true;
            if( value>=minTemp && value<=maxTemp) {
                chillHours++;
            }
        }
        if( counting && isEndDate(date)){
            LocalDateTime localDateTime=LocalDateTime.parse(date);
            addValue(chillHours, localDateTime.getYear() );
            chillHours =0;
        }
    }

}
