package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation.Values;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 *  A weather processor that calculates the first instance of some
 *  weather value below a certain value  and between dates
 *
 */@Component("FirstDateBelow")
@Scope("prototype")
public class FirstDateBelowValue extends DateValueProcessor {
    /**
     * the first value date
     */
    private Optional<LocalDateTime> date=Optional.empty();

    public FirstDateBelowValue() {
        super("First Date Below");
    }
    @Override
    public void before() {

        // set mode to above
        this.processorName="First Date Below "+ threshold;
        clearProcessedTextValues();
        yearlyDataValues.clear();
    }
    @Override
    protected void onEndDate(LocalDateTime date) {
        this.isInWindow();
    }

    public void onStop(LocalDateTime date) {
        String text = "First instance of " + dataType + " Below " + threshold;
        addValue(date,this.date, text);
        if(this.date.isPresent()){
            generateObservation(Values.time(this.date.get()));
        }

        this.date = Optional.empty();
        isInWindow();


    }
    @Override
    public void processWeatherBetween(double value, LocalDateTime date) {
        if( value<= threshold) {
            this.date=Optional.of(date);
          onStop(date);
        }
    }
}
