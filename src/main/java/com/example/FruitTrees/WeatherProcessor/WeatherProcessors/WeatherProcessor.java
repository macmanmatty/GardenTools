package com.example.FruitTrees.WeatherProcessor.WeatherProcessors;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.DailyValuesResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.MonthlyValuesResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
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
    private ArrayList<String> processedTextValues =new ArrayList<>();
    /**
     * the day to start processing weather
     */
   protected  int startDay=1;
    /**
     * the day to end processing weather
     */
   protected int  endDay=31;
    /**
     * the month to stat processing weather int from jan=1 to dec=12
     */
   protected  int startMonth =1;
    /**
     * the month to end processing weather int from jan=1 to dec=12
     */
   protected  int  endMonth=12;
    /**
     * the min max and value weather values used to process weather
     */
   protected double lowerBound;
   protected double upperBound;
   protected double threshold;
    /**
     * boolean to stop weather processing
     */
    boolean stopProcessing=false;
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
    protected List<String> dataTypes= new ArrayList<>();
    protected List<String> dataUnits= new ArrayList<>();

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
    /**
     * weather or not to calculate  mean the average
     */
    protected  boolean calculateMeanAverage;
    /**
     * weather or not to calculate median  the average
     */
    protected  boolean calculateMedianAverage;
    private boolean calculateMax;
    private boolean calculateMin;
    /**
     * temperature bins used for utah chill calculation
     * maybe empty to use default calculation mode
     */
    protected List<Bin> bins=new ArrayList<>();
    /**
     * station Id used for NOAA processing
     *
     */
        String stationId;
    /**
     * external processors required by this one
     */
    public List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = new ArrayList<>();
    public WeatherProcessor(String processorName) {
        this.processorName = processorName;
    }
    protected WeatherProcessor() {
    }
    /**
     * overridden  method called
     * before the processing of weather starts
     */
    public  void  before(){}
    /**
     * overridden  method called
     * after the processing of weather ends
     */
    public  void  after(){}
    /**
     *
     * internally called for each double in the data set to process weather value
     * implemented by each sub class that processes weather
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    protected abstract void processWeather(double value, LocalDateTime date);
    /**
     *
     * the externally called method for processing weather
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    public void processWeatherExternal(double value, LocalDateTime date){
        if(stopProcessing){
            return;
        }
        processWeather(value, date);
    }
    /**
     *
     * @param value
     * @param year
     */
    public void addProcessedTextValue(double value, int year){
        addProcessedTextValue(processorName +" for "+dataType+" "+year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ value);
    }
    /**
     *
     * @param text
     */
    public void addProcessedTextValue(String text){
        if(!onlyCalculateAverage) {
            this.processedTextValues.add(text);
        }
    }
    public void addAverageValue(String text){
        this.processedTextValues.add(text);
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
    public abstract void  calculateMeanAverageValue();
    /**
     * overridden method  used to calculate the average
     * of the processed weather values
     */
    public abstract void  calculateMedianAverageValue();
    public void calculateMinValue(){}
    public void calculateMaxValue(){}
    public ArrayList<String> getProcessedTextValues() {
        return processedTextValues;
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
    public void stopProcessing(){
        stopProcessing=true;
    }
    public void startProcessing(){
        stopProcessing=false;
    }
    public void clearProcessedTextValues() {
        processedTextValues.clear();
    }
    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
    public List<HourlyWeatherProcessRequest> getHourlyWeatherProcessRequests() {
        return hourlyWeatherProcessRequests;
    }
    public void setHourlyWeatherProcessRequests(List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests) {
        this.hourlyWeatherProcessRequests = hourlyWeatherProcessRequests;
    }
    public boolean isCalculateMeanAverage() {
        return calculateMeanAverage;
    }
    public void setCalculateMeanAverage(boolean calculateAverage) {
        this.calculateMeanAverage = calculateAverage;
    }
    public List<String> getDataTypes() {
        return dataTypes;
    }
    public void setDataTypes(List<String> dataTypes) {
        this.dataTypes = dataTypes;
    }
    public List<String> getDataUnits() {
        return dataUnits;
    }
    public void setDataUnits(List<String> dataUnits) {
        this.dataUnits = dataUnits;
    }
    public boolean isCalculateMax() {
        return calculateMax;
    }
    public void setCalculateMax(boolean calculateMax) {
        this.calculateMax = calculateMax;
    }
    public boolean isCalculateMin() {
        return calculateMin;
    }
    public void setCalculateMin(boolean calculateMin) {
        this.calculateMin = calculateMin;
    }
    public boolean isCalculateMedianAverage() {
        return calculateMedianAverage;
    }
    public void setCalculateMedianAverage(boolean calculateMedianAverage) {
        this.calculateMedianAverage = calculateMedianAverage;
    }
    public double getLowerBound() {
        return lowerBound;
    }
    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }
    public double getUpperBound() {
        return upperBound;
    }
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }
    public double getThreshold() {
        return threshold;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public List<Bin> getBins() {
        return bins;
    }

    public void setBins(List<Bin> bins) {
        this.bins = bins;
    }
}
