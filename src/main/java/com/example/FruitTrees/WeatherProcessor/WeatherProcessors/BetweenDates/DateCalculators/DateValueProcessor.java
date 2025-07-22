package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.ProcessWeatherBetweenDates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DateValueProcessor extends ProcessWeatherBetweenDates {
    public List<Optional<LocalDateTime>> yearlyDates=new ArrayList<>();
    protected  float percentMissing=.33f;
    @Override
    public void calculateAverage() {
        Optional<LocalDateTime> date = DateUtilities.calculateAverageDate(yearlyDates, percentMissing);
        if (date.isPresent()) {
            addAverageValue("Average " + processorName + " " + date.get());
        }
    else{
            addAverageValue("Average " + processorName + " " + "Too many dates where value was never reached!");
        }
    }

}
