package com.example.FruitTrees.ChillingHours;

import java.time.LocalDateTime;

public class DateUtilities {

    private DateUtilities() {
    }

    /**
     * check if a YYYY-MM-DD date has a time if not adds the 00:00 time to it.
     * and converts it to a java LocalDateTime object
     * @param date
     * @return
     */
    public static  LocalDateTime convertStringDateToDateTime(String date){

        return LocalDateTime.parse(convertStringDateStringDateTime(date));
    }

    /**
     * check if a YYYY-MM-DD date has a time if not adds the 00:00 time to it.

     * @param date
     * @return
     */
    public static  String convertStringDateStringDateTime(String date){
        if(!date.contains("T") &&  !date.contains("t") ){
            date=date+"T00:00";
        }
        return date;
    }

}
