package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
/**
 *  A weather processor that calculates the total amount of some
 * weather type usually rain fall or snow during given dates
 */
@Component("Total")
public class TotalCalculator extends ProcessWeatherBetweenDates{
    /**
     * the total amount of weather
     */
    private double total;
    public TotalCalculator() {
        super("Total");
    }
    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        super.yearlyDataValues.add(total);
        addProcessedValue(total, localDateTime.getYear() );
        total =0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        this.total = this.total + value;
    }
}
