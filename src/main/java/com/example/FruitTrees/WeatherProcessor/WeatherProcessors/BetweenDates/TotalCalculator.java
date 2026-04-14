package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Metrics.ConditionType;
import com.example.FruitTrees.Metrics.Observation.Values;
import com.example.FruitTrees.Metrics.Period;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
/**
 *  A weather processor that calculates the total amount of some
 * weather type usually rain fall or snow during given dates
 */
@Component("Total")
@Scope("prototype")

public class TotalCalculator extends ProcessWeatherBetweenDates{
    /**
     * the total amount of weather
     */
    private double total;
    public TotalCalculator() {
        super("Total");
        period= Period.YEARLY;

    }
    @Override
    protected void onEndDate(LocalDateTime date) {

        super.yearlyDataValues.add(total);
        addProcessedTextValue(total, date.getYear() );
        generateObservation(Values.number(total));

        total =0;
    }
    @Override
    protected void processWeatherBetween(double value,LocalDateTime date) {
        this.total = this.total + value;
    }
}
