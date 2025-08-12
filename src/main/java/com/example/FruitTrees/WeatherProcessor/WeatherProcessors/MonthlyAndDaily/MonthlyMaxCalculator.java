package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("MaxMonthly")
@Scope("prototype")

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
    protected void onMonthEnd(double value, LocalDateTime localDateTime) {
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));

            addProcessedTextValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name());
        monthlyValues.get(currentMonthName).add(finalValue);
        finalValue =Double.MIN_VALUE;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if (value > finalValue) {
            finalValue = value;
        }
    }
}
