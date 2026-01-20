package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import com.example.FruitTrees.Metrics.Observation.Values;
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
        this.isInWindow();
    }

    public void onStop(LocalDateTime date) {
            String text = "First instance of " + dataType + " Above " + threshold;
          addValue(date,this.date, text);
          if(this.date.isPresent()){
        generateObservation(Values.time(this.date.get()));
        }

        this.date=Optional.empty();
            isInWindow();

    }
    @Override
    public void processWeatherBetween(double value, LocalDateTime date) {
        if(value>= threshold){
            this.date=Optional.of(date);
            onStop(date);
        }
    }
}
