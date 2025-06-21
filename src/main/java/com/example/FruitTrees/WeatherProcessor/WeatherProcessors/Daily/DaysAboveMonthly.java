package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Daily;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily.DailyAndMonthlyWeatherProcessor;
import org.springframework.stereotype.Component;


/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  for each month
 *
 */
@Component("DaysAboveMonthly")
public class DaysAboveMonthly extends DailyAndMonthlyWeatherProcessor  {
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

    public DaysAboveMonthly() {
        super("Days Above");
    }
    @Override
    public void before() {
        super.before();
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException(" Params Array Size<1 You Must Include  The Minimum Value In The Array Of Parameters ");
        }

        super.processorName=" Days Above "+inputParameters.get(0)+" Monthly";
        this.valueToCheck = Double.parseDouble(inputParameters.get(0));
        clearProcessedTextValues();
    }

    @Override
    public void onStartDay(Number value, String date) {
        super.onStartDay(value, date);
    }

    @Override
    public void onEndDay(Number value, String date) {
        if(hours==24){
            totalDays++;
        }
        hours =0;
    }

    @Override
    protected void onMonthEnd(Number value, String date) {
        String text="Days " +dataType+  " Above "+ valueToCheck;
        monthlyValuesResponse.getValues().put(text, String.valueOf(totalDays));
        addProcessedTextValue(text + " For " + currentMonthName + " " + currentYear + " : " + hours);

        monthlyValues.get(currentMonthName).add(hours);
        hours =0;
        totalDays=0;

    }

    @Override
    protected void onStartNewMonth(Number value, String date) {

    }

    @Override
    protected void onEndYear(Number value, String date) {

    }

    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>= valueToCheck) {
            hours++;
        }
    }
}
