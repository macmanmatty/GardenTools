package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("MaxMonthly")
public class MonthlyMaxCalculator extends DailyAndMonthlyWeatherProcessor {
    private double finalValue =Double.MIN_VALUE;
    public MonthlyMaxCalculator() {
        super("Monthly Max");

    }

    @Override
    public void before() {
        super.before();
        processorName = "Monthly Max For "+dataType;

    }

    @Override
    protected void onMonthEnd(Number value, String date) {
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));
        LocalDateTime localDateTime=LocalDateTime.parse(date);
            addProcessedTextValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name());
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
