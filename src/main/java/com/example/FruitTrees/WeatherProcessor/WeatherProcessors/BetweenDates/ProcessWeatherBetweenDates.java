package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 *  base class for weather data processor that process data between dates yearly or semi-yearly weather between dates
 */
public abstract  class ProcessWeatherBetweenDates  extends WeatherProcessor {
    /**
     * the processed values for each year or semi year
     */
    protected List<Double>  yearlyDataValues=new ArrayList<>();

    /**
     *  this is true if  the weather falls between the given dates
     *  and the weather data is currently processing
     */
    private boolean processing;
    public ProcessWeatherBetweenDates(String name) {
        super(name);
    }

    protected ProcessWeatherBetweenDates() {
    }

    @Override
    public void before() {
        clearProcessedTextValues();
        yearlyDataValues.clear();
    }
    /**
     * the overridden process data method that  processes 
     * weather data between dates
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    @Override
    public final  void processWeather(Number value, String date) {
        switch(DateUtilities.checkDate(date, startDay, startMonth, endDay, endMonth)){
                case START_PROCESSING -> {
                    processing =true;
                    onStartDate(date);
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
     * method  for
     * preforming actions on weather start date
     * when the processing of weather starts
     * @param date  the current date and time of the weather  being processed
     * 
     */
    protected  void onStartDate(String date){}
    /**
     *  method  for
     * preforming actions on weather end date
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected void onEndDate(String date){

    }
    /**
     * subclass implemented method  for
     * processing the weather
     * @param date  the current date and time of the weather  being processed
     * @param  data the value of the weather data at the current date and time
     */
    protected abstract void processWeatherBetween(Number data, String date);


    @Override
    public void calculateMeanAverageValue() {
        double total=0;
       for( Double doubleNum: yearlyDataValues){
          total= doubleNum+total;
        }
       double average=Math.round(total/yearlyDataValues.size());
       addAverageValue("Average "+ processorName +" "+average);
    }


    /**
     * stops all processing of data
     */
    @Override
    public void stopProcessing(){
        processing=false;
    }
    @Override
    public void startProcessing(){
        processing=true;
    }


}
    
