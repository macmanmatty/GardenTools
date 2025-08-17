package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 *  A weather processor that calculates the first instance of some
 *  weather value above a certain value  and between dates
 *
 */@Component("FirstDateAbove")
@Scope("prototype")
public class FirstDateAboveValue extends DateValueProcessor {
    /**
     * the first value date
     */
    private Optional<LocalDateTime> date=Optional.empty();

    public FirstDateAboveValue() {
        super("First Date Above");
    }
    @Override
    public void before() {

        // set mode to above
        this.processorName="First Date Above "+ threshold;
        clearProcessedTextValues();
        yearlyDataValues.clear();

    }

    @Override
    protected void onEndDate(LocalDateTime date) {
        startProcessing();
    }

    public void onStop(LocalDateTime date) {
            String text = "First instance of " + dataType + " Above " + threshold;
          addValue(date,this.date, text);
            this.date=Optional.empty();
            stopProcessing();

    }
    @Override
    public void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        if(value>= value){
            this.date=Optional.of(date);
            onStop(date);
        }
    }
}
