package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import com.example.FruitTrees.Metrics.Observation.Values;
import com.example.FruitTrees.Metrics.Period;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 *  A weather processor that calculates the last instance  of some
 *  weather value above a certain value  and between dates
 *
 */@Component("LastDateAbove")
@Scope("prototype")

public class LastDateAboveValue extends DateValueProcessor {
    /**
     * the first value date
     */
    private Optional<LocalDateTime> date=Optional.empty();



    public LastDateAboveValue() {
        super("Last Date Above");
    }
    @Override
    public void before() {
        String mode="below";
        this.processorName="Last Date With Value Above "+mode+" "+ threshold;
        clearProcessedTextValues();
        yearlyDataValues.clear();
        period= Period.YEARLY;


    }

    @Override
    public void onEndDate(LocalDateTime date) {
            String text = "Last instance of " + dataType + " Above " + threshold;
            addValue(date,this.date, text);
        if(this.date.isPresent()){
            generateObservation(Values.time(this.date.get()));
        }

        this.date = Optional.empty();
    }
    @Override
    public void processWeatherBetween(double value, LocalDateTime date) {
        if( value>= threshold) {
            this.date= Optional.of(date);
        }
    }
}
