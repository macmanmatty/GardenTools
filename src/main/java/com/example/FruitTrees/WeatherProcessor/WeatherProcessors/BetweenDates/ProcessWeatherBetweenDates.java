package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DateType;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;

import java.time.LocalDateTime;
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
        values.clear();
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
            switch(checkDate(date)){
                case START_PROCESSING -> {
                    processing =true;
                    onStartDate(date);
                    break;
                }
                case END_PROCESSING -> {
                    processing =false;
                    onEndDate(date);
                    break;
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
    abstract void processWeatherBetween(Number data, String date);

    /**
     *  checks a string  date in YYYY-MM-DDTHH:MM format   to see if
     *  it is the start date or the end date or justa normal date
     * @param openMeteoDateAndTime
     * @return
     */
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

    @Override
    public void calculateAverage() {
        double total=0;
       for( Double doubleNum: yearlyDataValues){
          total= doubleNum+total;
        }
       double average=Math.round(total/yearlyDataValues.size());
       values.add("Average "+ processorName +" "+average);
       //currentYearlyValuesResponse.getValues().put("Average "+dataType+ " for ", String.valueOf(average));
    }


}
    
