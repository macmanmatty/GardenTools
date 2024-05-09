package com.example.FruitTrees.WeatherProcessor.WeatherProcessors;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.DailyValuesResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.MonthlyValuesResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 *a base  abstract class for implementing a weather processor
 * identified by the component name in the map of weather processors
 * in the WeatherDataProcessor class
 */
@Scope("prototype")
@Component
public abstract class WeatherProcessor {
    /**
     *  the processed string values for the weather
     */
    protected ArrayList<String> values=new ArrayList<>();
    /**
     * the day to start processing weather
     */
   protected  int startDay=1;
    /**
     * the day to end processing weather
     */
   protected int  endDay=31;
    /**
     * the month to stat processing weather
     */
   protected  int startMonth =1;
    /**
     * the month to end processing weather
     */
   protected  int  endMonth=12;

    /**
     * the year weather was started processing on
     */
    protected int startYear;

    /**
     * the year weather was ended processing on
     */
    protected int endYear;
    /**
     * the name of the processor can be different
     * from the same as the spring  component name;
     *
     */
   protected String processorName;
    /**
     * the type of open meteo data the processor is currently processing
     */
  protected String dataType="";
    /**
     * the current of measurement for the data being processed
     */
    protected String dataUnit="";
    /**
     * the list of input values used to process the weather
     */
   protected  List<String> inputParameters = new ArrayList<>();
    /**
     * the weather response object for adding data
     */
    protected LocationWeatherResponse locationWeatherResponse;
    /**
     * the  response  object for the yearly values
     */
    protected YearlyValuesResponse currentYearlyValuesResponse;
    /**
     * the  response  object for the monthly values
     */
    protected MonthlyValuesResponse monthlyValuesResponse;
    /**
     * the  response  object for the daily values
     */
    protected DailyValuesResponse dailyValues;

    /**
     * weather or not to only calculate the average
     */
    protected  boolean onlyCalculateAverage;


    public WeatherProcessor(String processorName) {
        this.processorName = processorName;
    }

    protected WeatherProcessor() {
    }

    /**
     * overridden  method called
     * before the processing of weather starts
     */
    public  void  before(){};
    /**
     * overridden  method called
     * after the processing of weather ends
     */
    public  void  after(){};
    /**
     *
     * called for each double in the data set to process weather value
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    public abstract void processWeather(Number value, String date);

    /**
     *
     * @param value
     * @param year
     */

    public void addProcessedValue(double value, int year){
        addProcessedValue(processorName +" for "+dataType+" "+year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ value);
    }
    /**
     *
     * @param text
     */
    public void addProcessedValue(String text){
        if(!onlyCalculateAverage) {
            values.add(text);
        }
    }

    /**
     * checks to see if the month and day given as month and day of a date   for processing data
     * is valid if the date is 2/29 converts it to 3/1
     * @param month the numeric month value to check
     * @param day the numeric day value to check
     * @return the new day and  month value
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
    /**
     * overridden method  used to calculate the average
     * of the processed weather values
     */
    public void calculateAverage(){}
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
    public String getProcessorName() {
        return processorName;
    }
    public List<String> getInputParameters() {
        return inputParameters;
    }
    public void setInputParameters(List<String> inputParameters) {
        this.inputParameters = inputParameters;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getDataUnit() {
        return dataUnit;
    }
    public void setDataUnit(String dataUnit) {
        this.dataUnit = dataUnit;
    }
    public LocationWeatherResponse getLocationWeatherResponse() {
        return locationWeatherResponse;
    }
    public void setLocationWeatherResponse(LocationWeatherResponse locationWeatherResponse) {
        this.locationWeatherResponse = locationWeatherResponse;
    }

    public boolean isOnlyCalculateAverage() {
        return onlyCalculateAverage;
    }

    public void setOnlyCalculateAverage(boolean onlyCalculateAverage) {
        this.onlyCalculateAverage = onlyCalculateAverage;
    }
}
