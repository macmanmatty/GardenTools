package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.stereotype.Component;

/**
 *  A weather processor that calculates the total amount of some
 *  weather value between two  values  and between dates usually used  for counting chilling hours (temperature ) for deciduous fruit trees
 * which  are generally assumed to be  1 chill hour for every hour above 32 and below 45 degrees
 * from 11/1 to 3/31
 */
@Component("HoursBetween")
public class HoursBetweenCalculator extends ProcessWeatherBetweenDates {
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
    public HoursBetweenCalculator() {
    }
    @Override
    public void before() {
        if(inputParameters.size()<2){
            throw new IllegalArgumentException("Missing min and max parameters");
        }
        this.processorName="Hours Between "+inputParameters.get(0)+" And "+inputParameters.get(1);

        this.minTemp= Double.parseDouble(inputParameters.get(0));
        this.maxTemp= Double.parseDouble(inputParameters.get(1));
        clearProcessedTextValues();
        yearlyDataValues.clear();

    }
    @Override
    protected void onEndDate(String date) {
        int year= DateUtilities.getYear(date);
        super.yearlyDataValues.add(chillHours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String years= text+ " Above "+minTemp+" And Below "+maxTemp;
        yearlyValuesResponse.getValues().put(years, String.valueOf(chillHours));
        addProcessedTextValue(years+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ chillHours);
        chillHours =0;
    }

    /**
     *
     * @param data the value of the weather data at the current date and time
     * @param date  the current date and time of the weather  being processed
     */
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>=minTemp && value<=maxTemp) {
            chillHours++;
        }
    }
}
