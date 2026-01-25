package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Metrics.ConditionType;
import com.example.FruitTrees.Metrics.Unit;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.Metrics.Observation.Values;
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
    private double hours;

    public HoursBetweenCalculator() {
        canonicalUnit= Unit.HOUR;
        conditionType= ConditionType.HOURS_WHERE;


    }
    @Override
    public void before() {

        this.processorName="Hours Between "+super.upperBound +" And "+super.lowerBound+" Of "+dataType;
        clearProcessedTextValues();
        yearlyDataValues.clear();

    }
    @Override
    protected void onEndDate(LocalDateTime date) {
        int year= date.getYear();
        super.yearlyDataValues.add(hours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String baseText= text+ " Above "+ lowerBound +" And Below "+ upperBound;
        yearlyValuesResponse.getValues().put(baseText, String.valueOf(hours));
        addProcessedTextValue(baseText+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ hours);
        generateObservation(Values.number(hours));

        hours =0;
    }

    /**
     *
     * @param value the value of the weather data at the current date and time
     * @param date  the current date and time of the weather  being processed
     */
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if( value>= super.lowerBound && value<= super.upperBound) {
            hours++;
        }
    }
}
