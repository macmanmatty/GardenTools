package com.example.FruitTrees.ChillingHours.WeatherProcessors.Monthly;
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
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        addValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name() );
        monthlyValues.get(currentMonthName).add(finalValue);
        finalValue =0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
            finalValue =finalValue+ value;
    }
}
