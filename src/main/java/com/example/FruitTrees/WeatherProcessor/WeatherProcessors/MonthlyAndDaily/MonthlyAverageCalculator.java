package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component("AverageMonthly")
@Scope("prototype")
public class MonthlyAverageCalculator
        extends DailyAndMonthlyWeatherProcessor {
    private double finalValue = 0;
    private int hours = 0;
    public MonthlyAverageCalculator() {
        super("Monthly Average");
    }
    @Override
    public void before() {
        super.before();
        processorName = "Monthly Average For " + dataType;
        finalValue = 0;
        hours = 0;
    }
    @Override
    protected void onMonthEnd(double value, LocalDateTime date) {
        if (hours > 0) {
            double average = finalValue / hours;
            monthlyValuesResponse.getValues().put(
                    processorName,
                    String.valueOf(average)
            );
            addProcessedTextValue(
                    average,
                    currentYear,
                    currentMonthName
            );
            monthlyValues.get(currentMonthName).add(average);
        }
        finalValue = 0;
        hours = 0;
    }
    @Override
    protected void processWeatherBetween(
            double value,
            LocalDateTime date) {
        if (Double.isFinite(value)) {
            finalValue += value;
            hours++;
        }
    }
}