package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value between two  values  and between dates usually used  for counting chilling hours (temperature ) for deciduous fruit trees
 * which  are generally assumed to be  1 chill hour for every hour above 32 and below 45 degrees
 * from 11/1 to 3/31
 */
@Component("HoursBetween")
@Scope("prototype")

public class HoursBetweenCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double chillHours;

    public HoursBetweenCalculator() {
    }
    @Override
    public void before() {

        this.processorName="Hours Between "+super.upperBound +" And "+super.lowerBound;
        clearProcessedTextValues();
        yearlyDataValues.clear();

    }
    @Override
    protected void onEndDate(LocalDateTime date) {
        int year= date.getYear();
        super.yearlyDataValues.add(chillHours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String years= text+ " Above "+ lowerBound +" And Below "+ upperBound;
        yearlyValuesResponse.getValues().put(years, String.valueOf(chillHours));
        addProcessedTextValue(years+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ chillHours);
        chillHours =0;
    }

    /**
     *
     * @param data the value of the weather data at the current date and time
     * @param date  the current date and time of the weather  being processed
     */
    @Override
    protected void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        if( value>= lowerBound && value<= upperBound) {
            chillHours++;
        }
    }
}
