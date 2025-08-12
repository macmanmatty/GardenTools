package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Daily;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily.DailyAndMonthlyWeatherProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  for each month
 *
 */
@Component("DaysBelowMonthly")
@Scope("prototype")

public class DaysBelowMonthly extends DailyAndMonthlyWeatherProcessor {
    /**
     * the counted hours
     */
    private double hours;
    /**
     * the min value
     */
    private double valueToCheck;

    /**
     * the number of days above a certain value
     */
    private double totalDays;


    public DaysBelowMonthly() {
        super("Days Above");
    }
    @Override
    public void before() {
        super.before();
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException(" Params Array Size<1 You Must Include  The Minimum Value In The Array Of Parameters ");
        }

        super.processorName="Days Below "+inputParameters.get(0)+" Monthly";
        this.valueToCheck = Double.parseDouble(inputParameters.get(0));
        clearProcessedTextValues();
    }

    @Override
    public void onStartDay(double value, LocalDateTime date) {
        super.onStartDay(value, date);
    }
    @Override
    public void onEndDay(Number value, LocalDateTime date) {
        if(hours==24){
            totalDays++;
        }
        hours =0;
    }

    @Override
    protected void onMonthEnd(double value, LocalDateTime date) {
        String text="Days " +dataType+  " Below "+ valueToCheck;
        monthlyValuesResponse.getValues().put(text, String.valueOf(totalDays));
        addProcessedTextValue(text + " For " + currentMonthName + " " + currentYear + " : " + hours);

        monthlyValues.get(currentMonthName).add(hours);
        hours =0;
        totalDays=0;
    }

    @Override
    protected void onStartNewMonth(double value, LocalDateTime date) {
         }

    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if( value <valueToCheck) {
            hours++;
        }
    }



}
