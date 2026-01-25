package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;

import com.example.FruitTrees.Metrics.ConditionType;
import com.example.FruitTrees.Metrics.Observation.Values;
import com.example.FruitTrees.Metrics.Unit;
import com.example.FruitTrees.Metrics.Units;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  for each month
 *
 */
@Component("HoursBelowMonthly")
@Scope("prototype")

public class HoursBelowMonthly extends DailyAndMonthlyWeatherProcessor {
    /**
     * the counted hours
     */
    private double hours;

    public HoursBelowMonthly() {
        canonicalUnit= Unit.HOUR;
        conditionType= ConditionType.HOURS_WHERE;    }
    @Override
    public void before() {
        super.before();
        super.processorName="Hours Below "+ threshold +" Monthly  Of "+dataType;
        clearProcessedTextValues();
    }

    @Override
    protected void onMonthEnd(double value, LocalDateTime date) {
        String text="Monthly Hours Of " +dataType+  " Below "+ this.threshold;
        super.processorName="Hours Below "+threshold+" Monthly";
        monthlyValuesResponse.getValues().put(text, String.valueOf(hours));
            addProcessedTextValue(text + " For " + currentMonthName + " " + currentYear + " : " + hours);

        monthlyValues.get(currentMonthName).add(hours);

        generateObservation(Values.number(value));


    hours = 0;

    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if( value<= super.threshold) {
            hours++;
        }
    }
}
