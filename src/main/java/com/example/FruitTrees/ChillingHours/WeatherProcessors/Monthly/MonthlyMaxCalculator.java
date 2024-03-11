package com.example.FruitTrees.ChillingHours.WeatherProcessors.Monthly;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValues;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("MaxMonthly")
public class MonthlyMaxCalculator extends MonthlyWeatherProcessor {
    private double finalValue =Double.MIN_VALUE;
    private YearlyValues yearlyValues;
    public MonthlyMaxCalculator() {
        super("Monthly Max");
    }
    
    @Override
    protected void onMonthEnd(Number value, String date) {
        currenMonthlyValues.getValues().put(name+" For "+dataType, String.valueOf(finalValue));
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        addValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name() );
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
