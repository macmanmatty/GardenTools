package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class WeatherProcessor {
    ArrayList<String> values=new ArrayList<>();
    int startDay=1;
     int  endDay=31;
    int startMonth =1;
    int  endMonth=12;
   private final  String name;

    /**
     * the lis of input values used to process the weather
     */
    List<Double> inputParameters = new ArrayList<>();

   private String dataName;

    public WeatherProcessor(String name) {
        this.name = name;
    }
    public abstract void  before();


    /**
     *
     * called for each double in the data set to process weather value
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    public abstract void processWeather(Number value, String date);
    public static String  [] getYearMonthAndDay(String date){
        String [] dateAndTime=date.split(":");
        String [] yearMonthAndDay=dateAndTime[0].split("-");
       return yearMonthAndDay;
    }

    /**
     * check to see if a date is between  the start
     * and end dates for processing weather
     * @param openMeteoDateAndTime the date and time string from the open meteo service
     * @return
     */
     boolean dateInRange( String openMeteoDateAndTime) {
         String parsedDate=openMeteoDateAndTime.split("T")[0];
        LocalDate localDate=LocalDate.parse(parsedDate);
        int day=localDate.getDayOfMonth();
        int month=localDate.getMonthValue();
         if (month > startMonth && month < endMonth) {
             return true;
         } else if (month == startMonth && month == endMonth) {
             // If the month is the same, check if the day is within the range
             return day >= startDay && day <= endDay;
         } else if (month == startMonth) {
             // If the month is the start month, check if the day is greater than or equal to the start day
             return day >= startDay;
         } else if (month == endMonth) {
             // If the month is the end month, check if the day is less than or equal to the end day
             return day <= endDay;
         } else {
             // If the month is neither the start nor end month, it's outside the range
             return false;
         }
    }

    /**
     * checks to see if  a given date equals the end date
     * @param openMeteoDateAndTime the date and time string from the open meteo service
     * @return
     */
    boolean isEndDate( String openMeteoDateAndTime) {
        String parsedDate=openMeteoDateAndTime.split("T")[0];
        LocalDate localDate=LocalDate.parse(parsedDate);
        int dayOfMonth=localDate.getDayOfMonth();
        int month=localDate.getMonthValue();
        if(dayOfMonth==endDay && month==endMonth){
            return true;
        }
        return false;
    }
    /**
     * checks to see if  a given date equals the start date
     * @param openMeteoDateAndTime the date and time string from the open meteo service
     * @return
     */
    boolean isStartDate( String openMeteoDateAndTime) {
        String parsedDate=openMeteoDateAndTime.split("T")[0];
        LocalDate localDate=LocalDate.parse(parsedDate);
        int dayOfMonth=localDate.getDayOfMonth();
        int month=localDate.getMonthValue();
        if(dayOfMonth==startDay && month==startMonth){
            return true;
        }
        return false;
    }
    public void addValue(double value, int year){
        values.add(name+" for "+year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ value);
    }

    /**
     * checks to see if the month and day given as month and day of a date   for processing data
     * is valid if the date is 2/29 converts it to 3/1
     * @param month
     * @param day
     * @return
     */
    public int [] dateCheck (int month, int day) {
        int [] monthAndDay=new int [2];

        if(month<1 || month>12){
            throw new IllegalArgumentException("month " +month +" is  out of range");
        }
        if(day<1 || day>31){
            throw new IllegalArgumentException("day " +day +" is  out of range");
        }
        if(day==29 && month==2){
            day=1;
            month=3;
        }
        monthAndDay[0]=month;
        monthAndDay[1]=day;
        return  monthAndDay;
    }


        public ArrayList<String> getValues() {
        return values;
    }
    public int getStartDay() {
        return startDay;
    }
    public void setStartMonthDay(int startMonth, int startDay) {
       int [] dates= dateCheck(startMonth, startDay);
        this.startDay =dates[1];
        this.startMonth =dates[0];
    }
    public int getEndDay() {
        return endDay;
    }
    public void setEndMonthDay(int endMonth, int endDay) {
        int [] dates= dateCheck(endMonth, endDay);
        this.endDay =dates[1];
        this.endMonth =dates[0];
    }
    public int getStartMonth() {
        return startMonth;
    }
    public int getEndMonth() {
        return endMonth;
    }
    public String getName() {
        return name;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public List<Double> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(List<Double> inputParameters) {
        this.inputParameters = inputParameters;
    }
}
