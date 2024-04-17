package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Monthly;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("MaxMonthly")
public class MonthlyMaxCalculator extends MonthlyWeatherProcessor {
    private double finalValue =Double.MIN_VALUE;
    public MonthlyMaxCalculator() {
        super("Monthly Max");
    }


    
    @Override
    protected void onMonthEnd(Number value, String date) {
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));
        LocalDateTime localDateTime=LocalDateTime.parse(date);
            addValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name());

        monthlyValues.get(currentMonthName).add(finalValue);

        finalValue =Double.MIN_VALUE;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if (value > finalValue) {
            finalValue = value;
        }
    }
}
