package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 *  A weather processor that calculates the last instance  of some
 *  weather value below a certain value  and between dates
 *
 */@Component("LastDateBelow")
@Scope("prototype")
public class LastDateBelowValue extends DateValueProcessor{
    /**
     * the first value date
     */
    private Optional<LocalDateTime> date=Optional.empty();


    @Override
    public void addValue(LocalDateTime date, Optional<LocalDateTime> endDate, String text) {
        super.addValue(date, endDate, text);
    }

    public LastDateBelowValue() {
        super("last Date Below");
    }
    @Override
    public void before() {
        // set mode to above\
        this.processorName="Last Date With Value Below "+ threshold;
        clearProcessedTextValues();
        yearlyDataValues.clear();

    }

    @Override
    public void onEndDate(LocalDateTime date) {
            String text = "Last instance of " + dataType + " Below " + threshold;
            addValue(date,this.date, text);
            this.date = Optional.empty();
    }
    @Override
    public void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        if( value<= value) {
            this.date= Optional.of(date);
        }
    }
}
