package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily;
import com.example.FruitTrees.Utilities.ArrayUtilities;
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

    public double currentValue;

    public  LocalDateTime currentDateTime;

    boolean onMonthEndCalled;

    /**
     * the map of  monthly values
     * key= string month name
     * value=  yearly data for month for the processed weather data
     */
    protected Map<String, List<Double>> monthlyValues=new LinkedHashMap<>();

    protected DailyAndMonthlyWeatherProcessor() {
    }

    @Override
    public void before() {
        clearProcessedTextValues();

        onMonthEndCalled = false;
        currentDateTime = null;
        currentValue = Double.NaN;

        currentYear = 0;
        currentMonth = 0;
        currentMonthName = "";
        missedHours = 0;

        monthlyValues.clear();

        for (java.time.Month month : java.time.Month.values()) {
            monthlyValues.put(month.name(), new ArrayList<>());
        }
    }

    @Override
    public void processWeather(double value, LocalDateTime date) {
        this.currentValue=value;
        this.currentDateTime=date;
        dateRecord = processTimestamp(date);

        // Initialize new year/month/day before processing.
        switch (dateRecord.dayType()) {
            case NEW_YEARS_DAY -> {
                onStartNewYear(value, date);
                onStartNewMonthLoop(value, date);
            }
            case FIRST_DAY_OF_MONTH ->
                    onStartNewMonthLoop(value, date);
            default -> {
            }
        }

        if (dateRecord.hourType() == DateType.START_DAY) {
            onStartDay(value, date);
        }

        // Process the hourly observation.
        if (Double.isFinite(value)) {
            processWeatherBetween(value, date);
        } else {
            missedHours++;
        }

        // Finalize the day after processing its last hour.
        if (dateRecord.hourType() == DateType.END_DAY) {
            onEndDay(value, date);
        }

        // Finalize the month/year after processing.
        switch (dateRecord.dayType()) {
            case NEW_YEARS_EVE -> {
                onMonthEndLoop(value, date);
                onEndYear(value, date);
            }
            case LAST_DAY_OF_MONTH ->
                    onMonthEndLoop(value, date);
            default -> {
            }
        }
    }

    @Override
    public void calculateMeanAverageValue() {
        for (Map.Entry<String, List<Double>> entry : monthlyValues.entrySet()) {
            String month = entry.getKey();

            DoubleSummaryStatistics stats = entry.getValue().stream()
                    .filter(Objects::nonNull)
                    .filter(Double::isFinite)
                    .mapToDouble(Double::doubleValue)
                    .summaryStatistics();

            double average = stats.getCount() > 0
                    ? Math.round(stats.getAverage())
                    : Double.NaN;

            addAverageValue(
                    "Average " + processorName +
                            " For Month " + month + " " + average
            );
        }
    }

    @Override
    public void calculateMedianAverageValue() {
        for (Map.Entry<String, List<Double>> entry : monthlyValues.entrySet()) {
            String month = entry.getKey();

            List<Double> values = entry.getValue().stream()
                    .filter(Objects::nonNull)
                    .filter(Double::isFinite)
                    .sorted()
                    .toList();

            double median = values.isEmpty()
                    ? Double.NaN
                    : ArrayUtilities.medianOfList(values);

            addAverageValue(
                    "Median Average For " + processorName +
                            " For Month " + month + " " + median
            );
        }
    }

    @Override
    public void after() {
        if (currentDateTime == null || onMonthEndCalled) {
            return;
        }

        onMonthEndLoop(currentValue, currentDateTime);
    }

    /**
     * Called at the start of a new day (typically at hour 0).
     * Override this method to perform any setup or initialization
     * required before processing the day's weather data.
     *
     * @param value the first weather value of the day
     * @param date the full date string (e.g., "2025-06-19T00:00:00")
     */
    public void onStartDay(double value, LocalDateTime date) {}

    /**
     * Called at the end of a day (typically at hour 23).
     * Override this method to finalize or summarize any
     * calculations for the day (e.g., count hours above a threshold).
     *
     * @param value the last weather value of the day
     * @param date the full date string (e.g., "2025-06-19T23:00:00")
     */
    public void onEndDay(Number value, LocalDateTime date) {}
    public void addProcessedTextValue(double value, int year, String month){
        addProcessedTextValue(processorName +" for "+dataType+" " +month+" "+year+  " : "+ value);
    }
    /**
     * subclass implemented method  for
     * preforming actions on weather  last hour of dec 31
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected  void onEndYear(Number value, LocalDateTime date){}
    /**
     * subclass implemented method  for
     * preforming actions on weather  the first hour jan 1
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected  void onStartNewYear(double value, LocalDateTime date) {}
    /**
     * subclass implemented method  for
     * preforming actions on weather first day of the first hour
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected void onStartNewMonthLoop(double value, LocalDateTime date){
        onStartNewMonth(value, date);

    }    /**
     * subclass implemented method  for
     * preforming actions on weather first day of the first hour
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected  void onStartNewMonth(double value, LocalDateTime date){}
    /**
     * subclass implemented method  for
     * preforming actions on weather last day of the  month on the 23 hour
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected void onMonthEndLoop(double value, LocalDateTime date){
        onMonthEndCalled =true;
        if (missedHours > 0) {
            warnings.add(
                    "Incomplete Data Set of " + dataType
                            + ": missing " + missedHours
                            + " hours for " + currentMonthName
                            + " " + currentYear
                            + ". Calculations may not be accurate."
            );
        }

        missedHours = 0;
        onMonthEnd(value, date);
    }

    /**
     * subclass implemented method  for
     * preforming actions on weather last day of the  month on the 23 hour
     * when the processing of weather ends
     * @param date  the current date and time of the weather  being processed
     */
    protected  void onMonthEnd(double value, LocalDateTime date){}
    /**
     * subclass implemented method  for
     * processing the weather
     *
     * @param value the value of the weather data at the current date and time
     * @param date  the current date and time of the weather  being processed
     */
    protected abstract void processWeatherBetween(double value, LocalDateTime date);
    /**
     * checks to see if date / time is  the 0 hour of the first day of the month
     * or the last day of the month or new years day or new years eve
     * if so sets the current years and month and returns a enum used to call
     * the correct abstract processing method
     *
     * @param localDate the current date and time being processed
     * @return true if the day / time is the 0 hour of the first day of the month
     * otherwise returns false
     */
    protected DateRecord processTimestamp(LocalDateTime localDate) {
        if (currentYear != localDate.getYear()
                || currentMonth != localDate.getMonthValue()) {
            onMonthEndCalled = false;
            currentYear = localDate.getYear();
            currentMonth = localDate.getMonthValue();
            currentMonthName = localDate.getMonth().name();

            currentYearlyValuesResponse =
                    locationWeatherResponse.getYearlyValues(
                            String.valueOf(currentYear)
                    );

            monthlyValuesResponse =
                    currentYearlyValuesResponse.getMonthlyValues(
                            currentMonthName
                    );
        }
        // Determine hour type
        DateType hourType = switch (localDate.getHour()) {
            case 0 -> DateType.START_DAY;
            case 23 -> DateType.END_DAY;
            default -> DateType.NORMAL_HOUR;
        };

        int day = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int hour = localDate.getHour();
        int maxDay = localDate.toLocalDate().lengthOfMonth();

        DateType dayType = DateType.STANDARD_DAY;

        if (month == 12 && day == 31 && hour == 23) {
            dayType = DateType.NEW_YEARS_EVE;

        } else if (month == 1 && day == 1 && hour == 0) {
            dayType = DateType.NEW_YEARS_DAY;

        } else if (day == maxDay && hour == 23) {
            dayType = DateType.LAST_DAY_OF_MONTH;

        } else if (day == 1 && hour == 0) {
            dayType = DateType.FIRST_DAY_OF_MONTH;
        }

        return new DateRecord(hourType, dayType);
    }


    
}
    
