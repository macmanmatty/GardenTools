package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Monthly;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DateType;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import java.time.LocalDateTime;
import java.util.*;
/**
 *  base class for weather data processor that processes yearly from 1/1 to 12/31 and   monthly weather
 * from 1/1 to 12/31.
 */
public abstract  class MonthlyWeatherProcessor extends WeatherProcessor {
    /**
     *  this is true if  the weather falls between the given dates
     *  and the weather data is currently processing
     */
    public MonthlyWeatherProcessor(String name) {
        super(name);
    }
    /**
     * the current year of weather being processed
     */
    public int currentYear;
    /**
     * the current numeric value  of the month  for weather being processed
     */
    public int currentMonth;
    /**
     * the current month name  of weather being processed
     */
    public String currentMonthName="";
    /**
     *  the total number of processed months
     */
    public int totalMonths;
    /**
     * the map of  monthly values
     * key= string month name
     * value=  yearly data for month for the processed weather data
     */
    Map<String, List<Double>> monthlyValues=new HashMap<>();

    protected MonthlyWeatherProcessor() {
    }

    @Override
    public void before() {
        values.clear();
        currentYearlyValuesResponse =locationWeatherResponse.getYearlyValues(String.valueOf(currentYear));
        monthlyValuesResponse = currentYearlyValuesResponse.getMonthlyValues(currentMonthName);
        monthlyValues.put("JANUARY", new ArrayList<>());
        monthlyValues.put("FEBRUARY", new ArrayList<>());
        monthlyValues.put("MARCH", new ArrayList<>());
        monthlyValues.put("APRIL", new ArrayList<>());
        monthlyValues.put("MAY", new ArrayList<>());
        monthlyValues.put("JUNE", new ArrayList<>());
        monthlyValues.put("JULY", new ArrayList<>());
        monthlyValues.put("AUGUST", new ArrayList<>());
        monthlyValues.put("SEPTEMBER", new ArrayList<>());
        monthlyValues.put("OCTOBER", new ArrayList<>());
        monthlyValues.put("NOVEMBER", new ArrayList<>());
        monthlyValues.put("DECEMBER", new ArrayList<>());
    }
    /**
     * the overridden process data method that  processes 
     * weather data between dates
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    @Override
    public void processWeather(Number value, String date) {
       switch( checkDate(date)){
           case NEW_YEARS_EVE -> {
               onEndYear(value, date);
               onMonthEnd(value, date);
           }
           case NEW_YEARS_DAY -> {
               onStartNewYear(value, date);
               onStartNewMonth(value, date);
           }
           case FIRST_DAY_OF_MONTH -> {
               onStartNewMonth(value, date);
               totalMonths++;
           }
           case LAST_DAY_OF_MONTH -> {
               onMonthEnd(value, date);
           }
       }
        processWeatherBetween(value, date);
        }
    @Override
    public void calculateAverage() {
      Set<String> monthNames= monthlyValues.keySet();
      for(String month:monthNames){
          double total=0;
          List<Double> monthlyValues=this.monthlyValues.get(month);
          for(Double doubleNum: monthlyValues){
              total=total+doubleNum;
          }
          double average=Math.round(total/monthlyValues.size());
          this.values.add("Average "+processorName+" For Month "+month+" "+average);
      }
    }
    public void addValue(double value, int year, String month){
        values.add(processorName +" for "+dataType+" " +month+" "+year+  " : "+ value);
    }
    /**
     * subclass implemented method  for
     * preforming actions on weather  last hour of dec 31
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected  void onEndYear(Number value, String date){}
    /**
     * subclass implemented method  for
     * preforming actions on weather  the first hour jan 1
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected  void onStartNewYear(Number value, String date) {}
    /**
     * subclass implemented method  for
     * preforming actions on weather first day of the first hour
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected  void onStartNewMonth(Number value, String date){}
    /**
     * subclass implemented method  for
     * preforming actions on weather last day of the  month on the 23 hour
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected  void onMonthEnd(Number value, String date){}
    /**
     * subclass implemented method  for
     * processing the weather
     * @param date  the current date and time of the weather  being processed
     * @param  data the value of the weather data at the current date and time
     */
    protected abstract void processWeatherBetween(Number data, String date);
    /**
     * checks to see if date / time is  the 0 hour of the first day of the month
     * or the last day of the month or new years day or new years eve
     * if so sets the current years and month and returns a enum used to call
     * the correct abstract processing method
     * @param openMeteoDateAndTime the current date and time being processed
     * @return true if the day / time is the 0 hour of the first day of the month
     * otherwise returns false
     */
    protected DateType checkDate(String openMeteoDateAndTime) {
        LocalDateTime localDate=LocalDateTime.parse(openMeteoDateAndTime);
        int maxDayOfMonth = localDate.toLocalDate().lengthOfMonth();
        int dayOfMonth=localDate.getDayOfMonth();
        int hour=localDate.getHour();
        int month=localDate.getMonthValue();
       if(dayOfMonth==31 && hour==23 &&month==12 ){
           return DateType.NEW_YEARS_EVE;
       }
       if(dayOfMonth==1 && hour==0 &&month==1 ){
           currentYear=localDate.getYear();
           currentMonth=1;
           currentMonthName=localDate.getMonth().name();
           currentYearlyValuesResponse =locationWeatherResponse.getYearlyValues(String.valueOf(currentYear));
           monthlyValuesResponse = currentYearlyValuesResponse.getMonthlyValues(currentMonthName);
           return DateType.NEW_YEARS_DAY;
       }
        if(dayOfMonth==maxDayOfMonth && hour==23){
            return DateType.LAST_DAY_OF_MONTH;
        }
       if(dayOfMonth==1 && hour==0){
           currentMonth=month;
           currentMonthName=localDate.getMonth().name();
           monthlyValuesResponse = currentYearlyValuesResponse.getMonthlyValues(currentMonthName);
           return DateType.FIRST_DAY_OF_MONTH;
       }
        return DateType.STANDARD_DAY;
    }
    
}
    
