package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import java.util.Locale;

public interface WeatherProcessor {
    double getValue();
    void processWeather(double value, String date);
    void setStartMonth(int month);
    void setEndMonth(int month);
    void setStartDay(int month);
    void setEndDay(int month);

    public static String  [] getYearMonthAndDay(String date){
        String [] dateAndTime=date.split(":");
        String [ ] yearMonthAndDay=dateAndTime[0].split("-");
       return yearMonthAndDay;
    }

}
