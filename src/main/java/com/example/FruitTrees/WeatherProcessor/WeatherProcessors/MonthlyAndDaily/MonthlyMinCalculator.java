package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("MinMonthly")
@Scope("prototype")

public class MonthlyMinCalculator extends DailyAndMonthlyWeatherProcessor {
    private double finalValue =Double.MAX_VALUE;
    public MonthlyMinCalculator() {
        super("Monthly Min");

    }

    @Override
    public void before() {
        super.before();
        processorName = "Monthly Min  For "+dataType;

    }

    @Override
    protected void onMonthEnd(double value, LocalDateTime localDateTime) {
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));

            addProcessedTextValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name());

        monthlyValues.get(currentMonthName).add(finalValue);

        finalValue =Double.MAX_VALUE;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if (value < finalValue) {
            finalValue = value;
        }
    }
}
