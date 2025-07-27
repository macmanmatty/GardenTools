package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
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
    /**
     * the min value
     */
    private double firstValue;
    public FirstDateBelowValue() {
    }
    @Override
    public void before() {
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException("Missing parameter");
        }
        this.firstValue = Double.parseDouble(inputParameters.get(0));
        // set mode to above
        this.processorName="First Date Below "+inputParameters.get(0);
        clearProcessedTextValues();
        yearlyDataValues.clear();
    }
    @Override
    protected void onEndDate(String date) {
        startProcessing();
    }

    public void onStop(String date) {
        String text = "First instance of " + dataType + " Below " + firstValue;
        addValue(date,this.date, text);
        this.date = Optional.empty();
        stopProcessing();


    }
    @Override
    public void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value<=firstValue) {
            this.date=Optional.of(LocalDateTime.parse(date));
          onStop(date);
        }
    }
}
