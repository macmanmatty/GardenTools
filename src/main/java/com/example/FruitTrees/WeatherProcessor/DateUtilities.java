package com.example.FruitTrees.WeatherProcessor;

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

}
