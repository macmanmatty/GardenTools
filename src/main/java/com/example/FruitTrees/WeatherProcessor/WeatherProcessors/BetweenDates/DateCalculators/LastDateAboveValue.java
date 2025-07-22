package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 *  A weather processor that calculates the last instance  of some
 *  weather value above a certain value  and between dates
 *
 */@Component("LastDateAbove")
public class LastDateAboveValue extends DateValueProcessor {
    /**
     * the first value date
     */
    private Optional<LocalDateTime> date=Optional.empty();
    /**
     * the min value
     */
    private double lastValue;


    public LastDateAboveValue() {
    }
    @Override
    public void before() {
        String mode="below";
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException("Missing parameter");
        }
        this.lastValue = Double.parseDouble(inputParameters.get(0));
        // set mode to above

        this.processorName="Last Date With Value Above "+mode+" "+inputParameters.get(0);
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
            String text = "Last instance of " + dataType + " Above " + lastValue;
            yearlyValuesResponse.getValues().put(text, localDate.toString());
            addProcessedTextValue(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + " was on  " + localDate.toLocalDate().toString() + " at " + localDate.getHour());
            this.date = Optional.empty();
        }
        else{
            int year= DateUtilities.getYear(date);
            String text = "Last instance of " + dataType + " Above " + lastValue;
            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            yearlyValuesResponse.getValues().put(text, "value never reached");
            addProcessedTextValue(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + " was never reached ");

        }
    }
    @Override
    public void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>= lastValue ) {
            this.date= Optional.of(LocalDateTime.parse(date));
        }
    }
}
