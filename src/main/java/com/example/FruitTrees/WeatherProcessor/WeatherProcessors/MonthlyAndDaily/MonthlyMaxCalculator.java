package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;
import com.example.FruitTrees.Metrics.Observation.Values;
import com.example.FruitTrees.Metrics.Period;
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
        period= Period.MONTHLY;

    }

    @Override
    protected void onMonthEnd(double value, LocalDateTime localDateTime) {
        monthlyValuesResponse.getValues().put(processorName +" For "+dataType, String.valueOf(finalValue));

            addProcessedTextValue(finalValue, localDateTime.getYear(), localDateTime.getMonth().name());
        monthlyValues.get(currentMonthName).add(finalValue);
        monthlyValuesData.add(finalValue);
        generateObservation(Values.number(finalValue));

        finalValue =Double.MIN_VALUE;
    }
    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        if (value > finalValue) {
            finalValue = value;
        }
    }
}
