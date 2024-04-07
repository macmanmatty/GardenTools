package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Daily;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DateType;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Monthly.MonthlyWeatherProcessor;

import java.time.LocalDateTime;

/**
 * class for weather data processor that processes yearly  monthly daily  weather from 1/1 to 12/31
 * from 1/1 to 12/31.
 */
public abstract  class DailyWeatherProcessor extends MonthlyWeatherProcessor {
    /**
     * this is true if  the weather falls between the given dates
     * and the weather data is currently processing
     *
     * @param name
     */
    public DailyWeatherProcessor(String name) {
        super(name);
    }
    @Override
    public void before() {
        values.clear();
        currentYearlyValuesResponse =locationWeatherResponse.getYearlyValues(String.valueOf(currentYear));
        monthlyValuesResponse = currentYearlyValuesResponse.getMonthlyValues(currentMonthName);
    }
    /**
     * the overridden process data method that  processes 
     * weather data between dates
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    @Override
    public void processWeather(Number value, String date) {
        DateType [] dateTypes=checkDateAndTime(date);
        switch (dateTypes[0]){
            
            case START_DAY -> {
                onStartDay(value, date);
                break;
                
            }
            case END_DAY -> {
                onEndDay(value, date);
            }
            
        }
        
        switch( dateTypes[1]){
            case NEW_YEARS_EVE -> {
                onEndYear(value, date);
                onMonthEnd(value, date);
                break;
            }
            case NEW_YEARS_DAY -> {
                onStartNewYear(value, date);
                onStartNewMonth(value, date);
                break;
            }
            case FIRST_DAY_OF_MONTH -> {
                onStartNewMonth(value, date);
                break;
            }
            case LAST_DAY_OF_MONTH -> {
                onMonthEnd(value, date);
                break;
            }
        }
        processWeatherBetween(value, date);
    }
    
    public void onStartDay(Number value, String date){}
    public void onEndDay(Number value, String date){}
    /**
     * checks to see if date / time is  the 0 hour of the first day of the month
     * or the last day of the month or new years day or new years eve
     * if so sets the current years and month and returns two enums used to call
     * first enum is  the  hour type START_DAY END_DAY or NORMAL_HOUR
     * used to call the daily methods
     * second  ENUMis used for monthly / yearly  processing
     * 
     * the correct abstract processing method
     * @param openMeteoDateAndTime the current date and time being processed
     * @return true if the day / time is the 0 hour of the first day of the month
     * otherwise returns false
     */
    protected DateType [] checkDateAndTime(String openMeteoDateAndTime) {
        DateType [] dateTypes = new DateType[1];
        LocalDateTime localDate=LocalDateTime.parse(openMeteoDateAndTime);
        dateTypes[0]=DateType.NORMAL_HOUR;
        int hour=localDate.getHour();
        if(hour==0){
            dateTypes[0]=DateType.START_DAY;
        }
        else if(hour==23){
            dateTypes[0]=DateType.END_DAY;
        }
        int maxDayOfMonth = localDate.toLocalDate().lengthOfMonth();
        int dayOfMonth=localDate.getDayOfMonth();
        int month=localDate.getMonthValue();
        if(dayOfMonth==31 && hour==23 &&month==12 ){
            
            dateTypes[1]= DateType.NEW_YEARS_EVE;
            return dateTypes;
        }
        if(dayOfMonth==1 && hour==0 &&month==1 ){
            currentYear=localDate.getYear();
            currentMonth=1;
            currentMonthName=localDate.getMonth().name();
            currentYearlyValuesResponse =locationWeatherResponse.getYearlyValues(String.valueOf(currentYear));
            dateTypes[1]= DateType.NEW_YEARS_DAY;
            return dateTypes;        }
        if(dayOfMonth==maxDayOfMonth && hour==23){
            dateTypes[1]= DateType.LAST_DAY_OF_MONTH;
            return dateTypes;
        }
        if(dayOfMonth==1 && hour==0){
            currentMonth=month;
            currentMonthName=localDate.getMonth().name();
            monthlyValuesResponse = currentYearlyValuesResponse.getMonthlyValues(currentMonthName);
            dateTypes[1]= DateType.FIRST_DAY_OF_MONTH;
            return dateTypes;        }
        dateTypes[1]= DateType.STANDARD_DAY;
        return dateTypes;    }

}
    
