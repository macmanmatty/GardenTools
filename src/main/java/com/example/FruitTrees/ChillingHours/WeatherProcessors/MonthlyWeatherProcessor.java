package com.example.FruitTrees.ChillingHours.WeatherProcessors;
import java.time.LocalDateTime;
/**
 * class for weather data processor that processes monthly weather
 * from 1/1 to 12/31.
 */
public abstract  class MonthlyWeatherProcessor extends WeatherProcessor {
    /**
     *  this is true if  the weather falls between the given dates
     *  and the weather data is currently processing
     */
    public MonthlyWeatherProcessor(String name) {
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
                if(isFirstDay(date)){
                    onStartNewMonth(value, date);
                }
                processWeatherBetween(value, date);
                if(isLastDay(date)){
              onMonthEnd(value, date);
                }
        }
    public void addValue(double value, int year, String month){
        values.add(name+" for "+dataType+" " +month+" "+year+  " : "+ value);
    }
    /**
     * subclass implemented method  for
     * preforming actions on weather first day of the first hour
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected abstract void onStartNewMonth(Number value, String date);
    /**
     * subclass implemented method  for
     * preforming actions on weather last day of the  month on the 23 hour
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected abstract void onMonthEnd(Number value, String date);
    /**
     * subclass implemented method  for
     * processing the weather
     * @param date  the current date and time of the weather  being processed
     * @param  data the value of the weather data at the current date and time
     */
    abstract void processWeatherBetween(Number data, String date);
    boolean isLastDay(String openMeteoDateAndTime) {
        LocalDateTime localDate=LocalDateTime.parse(openMeteoDateAndTime);
        int maxDayOfMonth = localDate.toLocalDate().lengthOfMonth();
        int dayOfMonth=localDate.getDayOfMonth();
        int hour=localDate.getHour();
        if(dayOfMonth==maxDayOfMonth && hour==23){
            return true;
        }
        return false;
    }
    boolean isFirstDay(String openMeteoDateAndTime) {
        LocalDateTime localDate=LocalDateTime.parse(openMeteoDateAndTime);
        int dayOfMonth=localDate.getDayOfMonth();
        int hour=localDate.getHour();
        if(dayOfMonth==1 && hour==0){
            return true;
        }
        return false;
    }
    
}
    
