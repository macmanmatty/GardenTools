package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.Utilities.ArrayUtilities;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DateType;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily.DailyAndMonthlyWeatherProcessor;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;

import java.time.LocalDateTime;
import java.time.YearMonth;
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
    private boolean processing;
    private YearMonth warningMonth;
    private  LocalDateTime currentDate;
    private boolean onEndDateCalled;

    public ProcessWeatherBetweenDates(String name) {
        super(name);
    }

    protected ProcessWeatherBetweenDates() {
    }
    @Override
    public void before() {
        super.before();
        yearlyDataValues.clear();

        processing = false;
        warningMonth = null;
        currentDate = null;
        onEndDateCalled = false;
        missedHours = 0;
    }
    /**
     * the overridden process data method that  processes 
     * weather data between dates
     * @param value the number value of the weather parameter
     * @param date the date and time the value happened
     */

    @Override
    public final void processWeather(double value, LocalDateTime date) {
        YearMonth observationMonth = YearMonth.from(date);
        this.currentDate = date;

        if (warningMonth != null && !warningMonth.equals(observationMonth)) {
            flushMissingHours();
        }

        warningMonth = observationMonth;
        DateType dateType = DateUtilities.checkDate(
                date, startDay, startMonth, endDay, endMonth
        );

        if (dateType == DateType.START_PROCESSING) {
            processing = true;
            onStartDateLoop(date);
        }

        if (processing) {
            if (Double.isFinite(value)) {
                processWeatherBetween(value, date);
            } else {
                missedHours++;
            }
        }

        if (dateType == DateType.END_PROCESSING && processing) {
            onEndDateLoop(date);
            processing = false;
        }
    }

    /**
     * method  for
     * preforming actions on weather start date
     * when the processing of weather starts
     * @param date  the current date and time of the weather  being processed
     * 
     */
    protected  void onStartDate(LocalDateTime date){}
    /**
     * method  for
     * preforming actions on weather start date
     * when the processing of weather starts
     * @param date  the current date and time of the weather  being processed
     *
     */
    protected  void onStartDateLoop(LocalDateTime date){
        onEndDateCalled=false;
        onStartDate(date);
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
     *  method  for
     * preforming actions on weather end date
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    private void onEndDateLoop(LocalDateTime date){
        onEndDateCalled = true;
        onEndDate(date);
    }
    /**
     * subclass implemented method  for
     * processing the weather
     * @param date  the current date and time of the weather  being processed
     * @param  value the value of the weather data at the current date and time
     */
    protected abstract void processWeatherBetween(double value, LocalDateTime date);


    @Override
    public void calculateMeanAverageValue() {
        double total=0;
       for( Double doubleNum: yearlyDataValues){
          total= doubleNum+total;
        }
        double average = yearlyDataValues.isEmpty()
                ? Double.NaN
                : Math.round(total / yearlyDataValues.size());
       addAverageValue("Mean Average For "+ processorName +" "+average);
    }
    @Override
    public void calculateMedianAverageValue() {
        if (yearlyDataValues.isEmpty()) {
            addAverageValue("Median Average For " + processorName + " NaN");
            return;
        }

        double median = ArrayUtilities.medianOfList(yearlyDataValues);
        addAverageValue("Median Average For " + processorName + " " + median);
    }

    @Override
    public void after() {
        flushMissingHours();

        if (processing && currentDate != null && !onEndDateCalled) {
            onEndDateLoop(currentDate);
            processing = false;
        }
    }

    /**
     * stops all processing of data
     */
    @Override
    public void stopProcessing(){
        processing=false;
    }
    @Override
    public void startProcessing(){
        processing=true;
    }
    private void flushMissingHours() {
        if (warningMonth != null && missedHours > 0) {
            warnings.add(
                    "Incomplete Data Set of " + dataType
                            + ": missing " + missedHours
                            + " hours for " + warningMonth
                            + ". Calculations may not be accurate."
            );
        }

        missedHours = 0;
    }


}
    
