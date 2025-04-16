package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Monthly;

import org.springframework.stereotype.Component;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  for each month
 *
 */
@Component("HoursBelowMonthly")
public class HoursBelowMonthly extends MonthlyWeatherProcessor {
    /**
     * the counted hours
     */
    private double hours;
    /**
     * the max value
     */
    private double maxValue;
    public HoursBelowMonthly() {
    }
    @Override
    public void before() {
        super.before();

        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException(" Params Array Size<1 You Must Include  The Maximum Value In The Array Of Parameters ");
        }
        super.processorName="Hours Below "+inputParameters.get(0)+" Monthly";
        this.maxValue = Double.parseDouble(inputParameters.get(0));
        values.clear();
    }

    @Override
    protected void onMonthEnd(Number value, String date) {
        String text="Monthly Hours Of " +dataType+  " Below "+ maxValue;
        super.processorName="Hours Below "+inputParameters.get(0)+" Monthly";
        monthlyValuesResponse.getValues().put(text, String.valueOf(hours));
            addProcessedValue(text + " For " + currentMonthName + " " + currentYear + " : " + hours);

        monthlyValues.get(currentMonthName).add(hours);
        hours =0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value<= maxValue) {
            hours++;
        }
    }
}
