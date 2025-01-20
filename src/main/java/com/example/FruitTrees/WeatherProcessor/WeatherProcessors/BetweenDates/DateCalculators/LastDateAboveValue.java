package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.ProcessWeatherBetweenDates;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *  A weather processor that calculates the last instance  of some
 *  weather value above a certain value  and between dates
 *
 */@Component("LastDateAboveValue")
public class LastDateAboveValue extends DateValueProcessor {
    /**
     * the first value date
     */
    private String date="value never reached";
    /**
     * the min value
     */
    private double lastValue;


    public LastDateAboveValue() {
    }
    @Override
    public void before() {
        String mode="below";
        if(inputParameters.size()<1){
            throw new IllegalArgumentException("Missing parameter");
        }
        this.lastValue = Double.parseDouble(inputParameters.get(0));
        // set mode to above

        this.processorName="Last Date With Value Above "+mode+" "+inputParameters.get(0);
        values.clear();
        yearlyDataValues.clear();

    }

    @Override
    protected void onEndDate(String date) {
        if (this.date != null) {
            LocalDateTime localDateTime = LocalDateTime.parse(this.date);
            int year = localDateTime.getYear();
            LocalDate localDate = localDateTime.toLocalDate();

            super.yearlyDates.add(localDate);

            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            String text = "Last instance of " + dataType + " Above " + lastValue;
            yearlyValuesResponse.getValues().put(text, localDate.toString());
            values.add(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + " was on  " + localDateTime.toLocalDate().toString() + " at " + localDateTime.getHour());
            this.date = null;
        }
        else{
            int year= DateUtilities.getYear(date);
            String text = "Last instance of " + dataType + " Above " + lastValue;
            YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
            yearlyValuesResponse.getValues().put(text, "value never reached");
            values.add(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + " was never reached ");

        }
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>= lastValue ) {
            this.date=date;
        }
    }
}
