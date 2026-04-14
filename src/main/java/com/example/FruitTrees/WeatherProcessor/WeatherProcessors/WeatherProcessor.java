package com.example.FruitTrees.WeatherProcessor.WeatherProcessors;
import com.example.FruitTrees.Metrics.*;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.DailyValuesResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.MonthlyValuesResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.Metrics.Period;
import com.example.FruitTrees.Metrics.Stat;
import com.example.FruitTrees.Metrics.Observation.Observation;
import com.example.FruitTrees.Metrics.Observation.ObservationCollector;
import com.example.FruitTrees.Metrics.Observation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    protected Unit outputUnit=Unit.SAME_AS_INPUT;
    /**
     * the current of measurement for the data being processed
     */
    protected Unit canonicalUnit=Unit.SAME_AS_INPUT;
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
        String stationId="";
    /**
     * the location id used for openmeteo
     *
     */
    protected String locationId="";
    /**
     * external processors required by this one
     */
    public List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = new ArrayList<>();
    /**
     *
     * the period of measure monthly daily, yearly
     */
    protected Period period=Period.NOT_SET;
    /**
     *
     * the  calculated stat mean median etc.
     */
    protected Stat stat=Stat.BASE;
    /**
     *
     * the location object
     */
    /**
     * the current year of weather being processed
     */
    public Integer currentYear;
    /**
     * the current numeric value  of the month  for weather being processed
     */
    protected Integer  currentMonth;
    /**
     * the current month name  of weather being processed
    /**
     * the current month name  of weather being processed
     */
    public String currentMonthName="";

    protected ConditionType conditionType;
    protected Comparison comparison;
    protected ObservationCollector observationCollector;
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
    protected void generateObservation(Value value) {
    generateObservation(value, Stat.BASE);
    }
    protected void generateObservation( Value value, Stat stat){
        observationCollector.add(new Observation(
                locationId,
                currentYear,
                currentMonth,
                outputUnit +" "+conditionType,
                dataType,
                value,
                outputUnit,
                stat,
                period,
                new Condition(conditionType,comparison, threshold, lowerBound, upperBound, outputUnit),
                new ComputationSpec(processorName, null, bins),
                Map.of(
                        "dataType", dataType,
                        "threshold", threshold,
                        "upperBound", upperBound,
                        "lowerBound", lowerBound,
                        "bins", bins,
                        "cmp", "lte",
                        "period", period.label(),
                        "stat", stat,
                        "location", locationWeatherResponse.getLocation()
                )
        ));
    }
    /**
     * checks to see if date / time is  the 0 hour of the first day of the month
     * or the last day of the month or new years day or new years eve
     * if so sets the current years and month and returns a enum used to call
     * the correct abstract processing method
     * @param localDate the current date and time being processed
     * @return true if the day / time is the 0 hour of the first day of the month
     * otherwise returns false
     */
    protected DateRecord analyzeDate(LocalDateTime localDate) {
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


    public Unit getCanonicalUnit() {
        return canonicalUnit;
    }

    public void setCanonicalUnit(Unit canonicalUnit) {
        this.canonicalUnit = canonicalUnit;
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
    public ObservationCollector getObservationCollector() {
        return observationCollector;
    }
    public void setObservationCollector(ObservationCollector observationCollector) {
        this.observationCollector = observationCollector;
    }
    public String getLocationId() {
        return locationId;
    }
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    public Period getPeriod() {
        return period;
    }
    public void setPeriod(Period period) {
        this.period = period;
    }
    public Stat getStat() {
        return stat;
    }
    public void setStat(Stat stat) {
        this.stat = stat;
    }




    public Unit getOutputUnit() {
        return outputUnit;
    }

    public void setOutputUnit(Unit outputUnit) {
        this.outputUnit = outputUnit;
    }


}
