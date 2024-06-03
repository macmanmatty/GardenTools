package com.example.FruitTrees.Utilities;

import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DateType;

import java.time.LocalDateTime;
import java.util.List;

public class DateUtilities {

    private DateUtilities() {
    }

    /**
     * check if a YYYY-MM-DD date has a time if not adds the 00:00 time to it.

     * @param date the date to check
     * @return the YYYY-MM-DD  date with a time of 0 hours and 0 minutes
     */
    public static  String convertStringDateStringDateTime(String date){
        if(!date.contains("T") &&  !date.contains("t") ){
            date=date+"T00:00";
        }
        return date;
    }

    /**
     * checks to see if a date is between  the start
     * and end dates for processing weather
     * @param currentMonth numeric month value
     * @param currentDay  the numeric day value
     * @return true if the date is range false if not
     */
    boolean dateInRange( int startMonth, int endMonth, int startDay, int endDay, int currentMonth, int currentDay) {
        if (currentMonth > startMonth && currentMonth < endMonth) {
            return true;
        } else if (currentMonth == startMonth && currentMonth == endMonth) {
            // If the month is the same, check if the day is within the range
            return currentDay >= startDay && currentDay <= endDay;
        } else if (currentMonth == startMonth) {
            // If the month is the start month, check if the day is greater than or equal to the start day
            return currentDay >= startDay;
        } else if (currentMonth == endMonth) {
            // If the month is the end month, check if the day is less than or equal to the end day
            return currentDay <= endDay;
        } else {
            // If the month is neither the start nor end month, it's outside the range
            return false;
        }
    }

    public int findDate(List<String> openMeteoDateAndTime, String startDateTime){
        String localDateTimeStart=DateUtilities.convertStringDateStringDateTime(startDateTime);
        int size=openMeteoDateAndTime.size();
        for(int count=0; count<size; count++){
            if(openMeteoDateAndTime.equals(localDateTimeStart)){
                return count;
            }
        }
        throw new IllegalArgumentException("Date: "+ startDateTime+ "Is  Out Of Bounds");
    }

    /**
     *  checks a string  date in YYYY-MM-DDTHH:MM format   to see if
     *  it is the start date or the end date or justa normal date
     * @param openMeteoDateAndTime
     * @return
     */
    public static  DateType checkDate(String openMeteoDateAndTime, int startDay,  int startMonth, int  endDay, int endMonth){
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


}
