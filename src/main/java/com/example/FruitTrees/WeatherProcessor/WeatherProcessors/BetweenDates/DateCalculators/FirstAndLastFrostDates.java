package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 *  A weather processor that calculates the first instance of some
 *  weather value above a certain value  and between dates
 *
 */@Component("FirstAndLastFrostDates")
@Scope("prototype")
public class FirstAndLastFrostDates extends DateValueProcessor {
    /**
     * the first frost date
     */
    private Optional<LocalDateTime> firstFrost =Optional.empty();
    /**
     * the min value
     */
    /**
     * the last frost date
     */
    private Optional<LocalDateTime> lastFrost=Optional.empty();
    /**
     * the min value
     */
    boolean calculateFirstFrost=false;
    int freezing;

    public FirstAndLastFrostDates() {
        super("First And Last Frost Dates");
        startDay=1;
        startMonth=1;
        endDay=1;
        endMonth=8;
        if(dataType.equals("celsius")){
            freezing=0;
        }
        else if (dataType.equals("fahrenheit")){
            freezing=32;
        }
        else{
            throw new IllegalArgumentException("This Processor Processes Temperature Data Only ");
        }

    }
    @Override
    public void before() {

        // set mode to above
        this.processorName="First Date Above "+threshold;
        clearProcessedTextValues();
        yearlyDataValues.clear();

    }

    @Override
    protected void onEndDate(LocalDateTime date) {
        startProcessing();
    }

    public void onStop(LocalDateTime date) {
            String text = "First instance of " + dataType + " Above " + lastFrost;
          addValue(date,this.firstFrost, text);
            this.firstFrost =Optional.empty();
            stopProcessing();
            calculateFirstFrost=true;
            endDay=31;
            endMonth=12;

    }
    @Override
    public void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        if(value<= freezing){
            this.firstFrost =Optional.of(date);
            onStop(date);
        }
    }
}
