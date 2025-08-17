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
@Component("DaysAboveYearly")
@Scope("prototype")

public class DaysAboveYearly extends DailyAndMonthlyWeatherProcessor {
    /**
     * the counted hours
     */
    private double hours;

    /**
     * the number of days above a certain value
     */
    private double totalDays;

    public DaysAboveYearly() {
        super("Days Above");
    }
    @Override
    public void before() {
        super.before();
        super.processorName=" Days Above "+ threshold +" Yearly";
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
    protected void onEndYear(Number value, LocalDateTime date) {
        String text="Days " +dataType+  " Above "+ this.threshold;
        currentYearlyValuesResponse.getValues().put(text, String.valueOf(totalDays));
        addProcessedTextValue(text + " For " + currentYear + " : " + totalDays);

        monthlyValues.get(currentMonthName).add(hours);
        hours =0;
        totalDays=0;

    }

    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if( value>= this.threshold) {
            hours++;
        }
    }
}
