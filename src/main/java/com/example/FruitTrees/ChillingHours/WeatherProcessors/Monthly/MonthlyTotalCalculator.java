package com.example.FruitTrees.ChillingHours.WeatherProcessors.Monthly;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.MonthlyValues;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValues;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("TotalMonthly")
public class MonthlyTotalCalculator extends MonthlyWeatherProcessor {
    private double finalValue =Double.MIN_VALUE;
    public MonthlyTotalCalculator() {
        super("Monthly Total");
    }
    @Override
    public void before() {
        super.before();
    }
   
    @Override
    protected void onMonthEnd(Number value, String date) {
        currenMonthlyValues.getValues().put(name+" For "+dataType, String.valueOf(finalValue));
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        addValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name() );
        finalValue =0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
            finalValue =finalValue+ value;
    }
}
