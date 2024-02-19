package com.example.FruitTrees.ChillingHours.WeatherProcessors.BetweenDates;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
/**
 *  A weather processor that calculates the total amount of some
 * value between values  and between dates usually used counting chilling hours for deciduous fruit trees
 * which  are generally assumed to be  1 chill hour for every hour above 32 and below 45
 * from 11/1 to 3/31
 */
@Component("MinMaxChill")
public class MinMaxChillCalculator  extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double chillHours;
    /**
     * the min value
     */
    private double minTemp;
    /**
     * the max value
     */
    private double maxTemp;
    public MinMaxChillCalculator() {
        super("Chill Hours");
    }
    @Override
    public void before() {
        if(inputParameters.size()<2){
            throw new IllegalArgumentException("Missing min and max parameters");
        }
        this.minTemp= Double.parseDouble(inputParameters.get(0));
        this.maxTemp= Double.parseDouble(inputParameters.get(1));
        values.clear();
    }
    @Override
    protected void onStartDate(String date) {
    }
    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        addValue(chillHours, localDateTime.getYear());
        chillHours =0;
    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>=minTemp && value<=maxTemp) {
            chillHours++;
        }
    }
}
