package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of
 *  counting chilling hours (temperature ) for deciduous fruit trees
 * using the Utah Method
 * from 11/1 to 3/31
 */
@Component("UtahChill")
public class UtahChillCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double chillHours;
    /**
     * the min value
     */
    private double minTemp;
    /**
     * the max value
     */
    private double maxTemp;
    public UtahChillCalculator() {
        super("  Utah Chill Hours");
    }
    @Override
    public void before() {
        if(inputParameters.size()<2){
            throw new IllegalArgumentException("Missing min and max parameters");
        }
        this.minTemp= Double.parseDouble(inputParameters.get(0));
        this.maxTemp= Double.parseDouble(inputParameters.get(1));
        values.clear();
        yearlyDataValues.clear();

    }
    @Override
    protected void onStartDate(String date) {
    }
    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        int year= localDateTime.getYear();
        super.yearlyDataValues.add(chillHours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String years= text+ " Utah Calculation Method ";
        yearlyValuesResponse.getValues().put(years, String.valueOf(chillHours));
        values.add(years+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ chillHours);
        chillHours =0;
    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>=34 && value<=45) {
            chillHours++;
        }
          else  if( value>=45 && value<=55) {
            chillHours=chillHours+.5;
        }
          else if(value>55 && value<=65){
              chillHours=chillHours-.5;
        }
        else if(value>65){
            chillHours--;
        }
    }
}
