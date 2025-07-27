package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DateRecord;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DateType;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import java.time.LocalDateTime;
import java.util.*;
/**
 *  base class for weather data processor that processes yearly from 1/1 to 12/31 and   monthly weather
 * from 1/1 to 12/31.
 */
public abstract  class DailyAndMonthlyWeatherProcessor extends WeatherProcessor {
    /**
     *  this is true if  the weather falls between the given dates
     *  and the weather data is currently processing
     */
    public DailyAndMonthlyWeatherProcessor(String name) {
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
    protected Map<String, List<Double>> monthlyValues=new HashMap<>();

    protected DailyAndMonthlyWeatherProcessor() {
    }

    @Override
    public void before() {
        clearProcessedTextValues();
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
        processWeatherBetween(value, date);
        DateRecord dateRecord=analyzeDate(date);
        switch (dateRecord.hourType()){
            case START_DAY -> {
                onStartDay(value, date);
                break;

            }
            case END_DAY -> {
                onEndDay(value, date);
            }

        }
       switch( dateRecord.dayType()){
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
        }
    @Override
    public void calculateMeanAverageValue() {
      Set<String> monthNames= monthlyValues.keySet();
      for(String month:monthNames){
          double total=0;
          List<Double> monthlyValues=this.monthlyValues.get(month);
          for(Double doubleNum: monthlyValues){
              total=total+doubleNum;
          }
          double average=Math.round(total/monthlyValues.size());
          this.addAverageValue("Average "+processorName+" For Month "+month+" "+average);
      }
    }

    /**
     * Called at the start of a new day (typically at hour 0).
     * Override this method to perform any setup or initialization
     * required before processing the day's weather data.
     *
     * @param value the first weather value of the day
     * @param date the full date string (e.g., "2025-06-19T00:00:00")
     */
    public void onStartDay(Number value, String date) {}

    /**
     * Called at the end of a day (typically at hour 23).
     * Override this method to finalize or summarize any
     * calculations for the day (e.g., count hours above a threshold).
     *
     * @param value the last weather value of the day
     * @param date the full date string (e.g., "2025-06-19T23:00:00")
     */
    public void onEndDay(Number value, String date) {}
    public void addProcessedTextValue(double value, int year, String month){
        addProcessedTextValue(processorName +" for "+dataType+" " +month+" "+year+  " : "+ value);
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
    protected DateRecord analyzeDate(String openMeteoDateAndTime) {
        LocalDateTime localDate = LocalDateTime.parse(openMeteoDateAndTime);

        // Determine hour type
        DateType hourType = switch (localDate.getHour()) {
            case 0 -> DateType.START_DAY;
            case 23 -> DateType.END_DAY;
            default -> DateType.NORMAL_HOUR;
        };

        // Determine day type
        int day = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int maxDay = localDate.toLocalDate().lengthOfMonth();
        DateType dayType = DateType.STANDARD_DAY;

        if (day == 31 && localDate.getHour() == 23 && month == 12) {
            dayType = DateType.NEW_YEARS_EVE;
        } else if (day == 1 && localDate.getHour() == 0 && month == 1) {
            currentYear = localDate.getYear();
            currentMonth = 1;
            currentMonthName = localDate.getMonth().name();
            currentYearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(currentYear));
            monthlyValuesResponse = currentYearlyValuesResponse.getMonthlyValues(currentMonthName);
            dayType = DateType.NEW_YEARS_DAY;
        } else if (day == maxDay && localDate.getHour() == 23) {
            dayType = DateType.LAST_DAY_OF_MONTH;
        } else if (day == 1 && localDate.getHour() == 0) {
            currentMonth = month;
            currentMonthName = localDate.getMonth().name();
            monthlyValuesResponse = currentYearlyValuesResponse.getMonthlyValues(currentMonthName);
            dayType = DateType.FIRST_DAY_OF_MONTH;
        }

        return new DateRecord(hourType, dayType);
    }
    
}
    
