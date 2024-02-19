package com.example.FruitTrees.ChillingHours.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.ChillingHours.WeatherProcessors.WeatherProcessor;

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
            if(isStartDateTime(date)){
                processing =true;
                onStartDate(date);
            }
            if(processing){
                processWeatherBetween(value, date);
              
            }
            if(isEndDateTime(date) && processing){
                processing =false;
                onEndDate(date);
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
    
}
    
