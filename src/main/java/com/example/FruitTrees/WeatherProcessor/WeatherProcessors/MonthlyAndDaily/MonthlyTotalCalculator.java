package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("TotalMonthly")
@Scope("prototype")

public class MonthlyTotalCalculator extends DailyAndMonthlyWeatherProcessor {
    private double finalValue =0;
    public MonthlyTotalCalculator() {
        super("Monthly Total");

    }
    @Override
    public void before() {
        super.before();
        processorName = "Monthly Total  For "+dataType;

    }
   
    @Override
    protected void onMonthEnd(Number value, String date) {
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));
        LocalDateTime localDateTime=LocalDateTime.parse(date);
            addProcessedTextValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name());
        monthlyValues.get(currentMonthName).add(finalValue);
        finalValue =0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
            finalValue =finalValue+ value;
    }
}
