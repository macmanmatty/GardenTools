package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;
import com.example.FruitTrees.Utilities.ArrayUtilities;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.ProcessWeatherBetweenDates;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
public abstract class DateValueProcessor extends ProcessWeatherBetweenDates {
    public List<Optional<LocalDateTime>> yearlyDates=new ArrayList<>();
    protected  float percentMissing=.33f;

    public DateValueProcessor(String name) {
        super(name);
    }

    @Override
    public void calculateMeanAverageValue() {
        Optional<LocalDateTime> date = DateUtilities.calculateMeanAverageDate(yearlyDates, percentMissing);
        if (date.isPresent()) {
            addAverageValue("Mean Average For " + processorName + " " + date.get());
        }
    else{
            addAverageValue("Mean Average " + processorName + " " + "Too many dates where value was never reached!");
        }
    }

    @Override
    public void calculateMedianAverageValue() {
        Optional<LocalDateTime> date = DateUtilities.calculateMedianAverageDate(yearlyDates, percentMissing);
        if (date.isPresent()) {
            addAverageValue("Median verage " + processorName + " " + date.get());
        }
        else{
            addAverageValue("Median Average " + processorName + " " + "Too many dates where value was never reached!");
        }
    }
   public void addValue( LocalDateTime date, Optional<LocalDateTime> endDate, String text){
           if (endDate.isPresent()) {
               LocalDateTime localDate =endDate.get();
               int year = localDate.getYear();
              yearlyDates.add(endDate);
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
               String formattedDate = localDate.format(formatter);
               YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
               yearlyValuesResponse.getValues().put(text, localDate.toString());
               addProcessedTextValue(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + " was on  " +formattedDate);
           }
           else{
               int year= date.getYear();
               YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
               yearlyValuesResponse.getValues().put(text, "value never reached");
               addProcessedTextValue(text + year + " from: " + startMonth + "/" + startDay + " to " + endMonth + "/" + endDay + " was never reached ");
           }
       }
    
}
