package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MultipleDataSet;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.Utilities.DataUtilities;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value above a certain value  and between dates
 *
 */
@Component("FrostLine")
@Scope("prototype")

public class FrostLineCalculator extends ProcessMultipleWeatherDataSetsBetweenDates {
    /**
     * the min frost line
     */
   private String frostLine ="No Frost Line";
    public FrostLineCalculator() {
    }
    @Override
    public void before() {
        if(inputParameters.isEmpty()){
            throw new IllegalArgumentException("Parameter");
        }
        this.processorName="Hours Above "+inputParameters.get(0);
        clearProcessedTextValues();
        yearlyDataValues.clear();
    }

    @Override
    public void calculateMedianAverageValue() {

    }

    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        int year= localDateTime.getYear();
       YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Frost Line is below "+ frostLine;
        yearlyValuesResponse.getValues().put(text, String.valueOf(frostLine));
        frostLine ="No Frost Line";
    }
    @Override
    void processWeatherBetween(List<Number> data, List<String> dataType, String date) {
        int size=data.size();
       for(int count=0; count<size; count++){
           double value=data.get(0).doubleValue();
           if(value<32d){
               this.frostLine= DataUtilities.extractValuesForSoil(dataType.get(count));
           }

       }
    }
}
