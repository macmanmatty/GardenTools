package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;
import com.example.FruitTrees.Utilities.ArrayUtilities;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.Metrics.Period;
import com.example.FruitTrees.Metrics.Stat;
import com.example.FruitTrees.Metrics.Observation.Values;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *  base class for weather data processor that process data between dates yearly or semi-yearly weather between dates
 */
public abstract  class ProcessWeatherBetweenDates  extends WeatherProcessor {
    /**
     * the processed values for each year or semi year
     */
    protected List<Double>  yearlyDataValues=new ArrayList<>();
    /**
     *  this is true if  the weather falls between the given dates
     *  and the weather data is currently processing
     */
    private boolean inWindow;

    private boolean sawStart;
    public ProcessWeatherBetweenDates(String name) {
        super(name);
    }
    protected ProcessWeatherBetweenDates() {
        period= Period.YEARLY;
    }
    @Override
    public void before() {
        clearProcessedTextValues();
        yearlyDataValues.clear();
    }
    /**
     * the overridden process data method that  processes 
     * weather data between dates
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */
    @Override
    public final  void processWeather(double value, LocalDateTime date) {
        switch(DateUtilities.checkDate(date, startDay, startMonth, endDay, endMonth)){
                case START_PROCESSING -> {
                    inWindow =true;
                    currentYear = date.getYear();
                    onStartDate(date);
                    sawStart=true;
                }
                case END_PROCESSING -> {
                    if(!sawStart){
                        generateObservation(Values.text("Incomplete Data Set!"));
                    }
                    sawStart=false;
                    inWindow =false;
                    onEndDate(date);
                }
            }
            if(inWindow){
                processWeatherBetween(value, date);
            }
        }
    /**
     * method  for
     * preforming actions on weather start date
     * when the processing of weather starts
     * @param date  the current date and time of the weather  being processed
     * 
     */
    protected  void onStartDate(LocalDateTime date){
    }
    /**
     *  method  for
     * preforming actions on weather end date
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected void onEndDate(LocalDateTime date){
    }
    /**
     * subclass implemented method  for
     * processing the weather
     * @param date  the current date and time of the weather  being processed
     * @param  data the value of the weather data at the current date and time
     */
    protected abstract void processWeatherBetween(double data, LocalDateTime date);
    @Override
    public void calculateMeanAverageValue() {
     double mean=ArrayUtilities.meanOfList(yearlyDataValues);
        generateObservation(Values.number(mean), Stat.MEAN);
        addAverageValue("Mean Average For "+ processorName +" "+mean);
        generateObservation(Values.number(mean), Stat.MEDIAN);


    }
    @Override
    public void calculateMedianAverageValue() {
        double average=ArrayUtilities.medianOfList(yearlyDataValues);
        generateObservation(Values.number(average), Stat.MEDIAN);
        addAverageValue(" Median Average For "+ processorName +" "+average);
        generateObservation(Values.number(average), Stat.MEDIAN);
    }
    @Override
    public void calculateMaxValue() {
        double max=ArrayUtilities.maxOfList(yearlyDataValues);
        generateObservation(Values.number(max), Stat.MAX);
        addAverageValue(" Median Average For "+ processorName +" "+max);
        generateObservation(Values.number(max), Stat.MEDIAN);
    }
    @Override
    public void calculateMinValue() {
        double min=ArrayUtilities.minOfList(yearlyDataValues);
        generateObservation(Values.number(min), Stat.MIN);
        addAverageValue(" Median Average For "+ processorName +" "+min);
        generateObservation(Values.number(min), Stat.MEDIAN);
    }
    public boolean isInWindow(){
        return inWindow;
    }
}
    
