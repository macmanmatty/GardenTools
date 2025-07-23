package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 *  A weather processor that calculates the first instance of some
 *  weather value below a certain value  and between dates
 *
 */@Component("FirstDateBelow")
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
    public void onEndDate(String date) {
        if (this.date.isPresent()) {
            LocalDateTime localDate = this.date.get();
            int year = localDate.getYear();
            super.yearlyDates.add(this.date);
            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            String text = "First instance of " + dataType + " Below " + firstValue;
            yearlyValuesResponse.getValues().put(text, localDate.toString());
            addProcessedTextValue(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + " was on  " + localDate.toLocalDate().toString() + " at " + localDate.getHour());
            this.date = Optional.empty();
        }
        else{
            int year= DateUtilities.getYear(date);
            String text = "First instance of " + dataType + " Below " + firstValue;
            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            yearlyValuesResponse.getValues().put(text, "value never reached");
            addProcessedTextValue(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + " was never reached ");

        }
    }
    @Override
    public void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value<=firstValue) {
            this.date=Optional.of(LocalDateTime.parse(date));
           terminate();
           onEndDate(date);
        }
    }
}
