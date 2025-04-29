package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.ProcessWeatherBetweenDates;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class DateValueProcessor extends ProcessWeatherBetweenDates {
    protected List<LocalDate> yearlyDates=new ArrayList<>();


    @Override
    public void calculateAverage() {
       LocalDate date= DateUtilities.calculateAverageDate(yearlyDates);
        addAverageValue("Average "+ processorName +" "+date);
    }


}
