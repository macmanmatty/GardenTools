package com.example.FruitTrees.ChillingHours.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.ChillingHours.WeatherProcessors.DateType;
import com.example.FruitTrees.ChillingHours.WeatherProcessors.WeatherProcessor;

import java.time.LocalDateTime;

/**
 * class for weather data processor hat process data between dates
 */
public abstract  class ProcessWeatherBetweenDates  extends WeatherProcessor {
    /**
     *  this is true if  the weather falls between the given dates
     *  and the weather data is currently processing
     */
    private boolean processing;
    public ProcessWeatherBetweenDates(String name) {
        super(name);
    }

    @Override
    public void before() {
        values.clear();
    }
    /**
     * the overridden process data method that  processes 
     * weather data between dates
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    @Override
    public final  void processWeather(Number value, String date) {
            switch(checkDate(date)){
                case START_PROCESSING -> {
                    processing =true;
                    onStartDate(date);
                    break;
                }
                case END_PROCESSING -> {
                    processing =false;
                    onEndDate(date);
                }
            }
            if(processing){
                processWeatherBetween(value, date);
            }
        }
    /**
     * subclass implemented method  for
     * preforming actions on weather start date
     * when the processing of weather starts
     * @param date  the current date and time of the weather  being processed
     * 
     */
    protected abstract void onStartDate(String date);
    /**
     * subclass implemented method  for
     * preforming actions on weather end date
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected abstract void onEndDate(String date);
    /**
     * subclass implemented method  for
     * processing the weather
     * @param date  the current date and time of the weather  being processed
     * @param  data the value of the weather data at he current date and time
     */
    abstract void processWeatherBetween(Number data, String date);


    public DateType checkDate(String openMeteoDateAndTime){
        LocalDateTime localDate=LocalDateTime.parse(openMeteoDateAndTime);
        int dayOfMonth=localDate.getDayOfMonth();
        int month=localDate.getMonthValue();
        int hour=localDate.getHour();
        if(dayOfMonth==endDay && month==endMonth && hour==23){
            return DateType.END_PROCESSING;
        }
        if(dayOfMonth==startDay && month==startMonth && hour==0){
            return DateType.START_PROCESSING;
        }
        return  DateType.STANDARD_DAY;
    }
    /**
     * checks to see if a date is between  the start
     * and end dates for processing weather
     * @param month numeric month value
     * @param day  the numeric day value
     * @return
     */
    boolean dateInRange(int month, int day) {
        if (month > startMonth && month < endMonth) {
            return true;
        } else if (month == startMonth && month == endMonth) {
            // If the month is the same, check if the day is within the range
            return day >= startDay && day <= endDay;
        } else if (month == startMonth) {
            // If the month is the start month, check if the day is greater than or equal to the start day
            return day >= startDay;
        } else if (month == endMonth) {
            // If the month is the end month, check if the day is less than or equal to the end day
            return day <= endDay;
        } else {
            // If the month is neither the start nor end month, it's outside the range
            return false;
        }
    }
}
    
