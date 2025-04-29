package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MultipleDataSet;

import com.example.FruitTrees.Utilities.DateUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 *  base class for weather data processor that process data between dates yearly or semi-yearly weather between dates
 */
public abstract  class ProcessMultipleWeatherDataSetsBetweenDates extends MultipleDataSetWeatherProcessor {
    /**
     * the processed values for each year or semi year
     */
    protected List<Double>  yearlyDataValues=new ArrayList<>();
    /**
     *  this is true if  the weather falls between the given dates
     *  and the weather data is currently processing
     */
    private boolean processing;

    protected ProcessMultipleWeatherDataSetsBetweenDates() {
    }

    @Override
    public void before() {
        clearProcessedTextValues();
        yearlyDataValues.clear();
    }
    /**
     * the overridden process data method that  processes 
     * weather data between dates
     * @param values the number value of the weather parameter
     * @param date the date and time the value happened
     */
    @Override
    public final  void processWeather(List<Number> values, List<String> dataTypes, String date) {
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
                processWeatherBetween(values,dataTypes, date);
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
    abstract void processWeatherBetween(List<Number> data, List<String> dataType, String date);


    @Override
    public void calculateAverage() {
        double total=0;
       for( Double doubleNum: yearlyDataValues){
          total= doubleNum+total;
        }
       double average=Math.round(total/yearlyDataValues.size());
       addAverageValue("Average "+ processorName +" "+average);
       //currentYearlyValuesResponse.getValues().put("Average "+dataType+ " for ", String.valueOf(average));
    }


}
    
