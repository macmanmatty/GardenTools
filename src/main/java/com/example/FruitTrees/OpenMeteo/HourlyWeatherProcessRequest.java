package com.example.FruitTrees.OpenMeteo;

import java.util.ArrayList;
import java.util.List;

public class HourlyWeatherProcessRequest {

    /**
     * the hourly open meteo  data  set process
     */
    private String hourlyDataType;
    /**
     *the name of the weather processor to use
     */
    private String processorName;
    /**
     * the day to start processing weather data on if calculating chill
     * CANNOT be FEB 29 2-29 FEB 29 will  be converted to MAR 1
     */

    private int startProcessDay;
    /**
     * the month to start processing weather  on if calculating chill
     * CANNOT be FEB 29 2-29 FEB 29 will  be converted to MAR 1
     */
    private int startProcessMonth;
    /**
     * the day  stop processing weather on if calculating chill
     * CANNOT be FEB 29 2-29 FEB 29 will  be converted to MAR 1
     */
    private int endProcessDay;
    /**
     * the month  stop processing weather  on if calculating chill
     * CANNOT be FEB 29 2-29 FEB 29 will  be converted to MAR 1
     */
    private int endProcessMonth;

    private List<Double> inputParameters= new ArrayList<>();



    public String getHourlyDataType() {
        return hourlyDataType;
    }

    public void setHourlyDataType(String hourlyDataType) {
        this.hourlyDataType = hourlyDataType;
    }

    public int getStartProcessDay() {
        return startProcessDay;
    }

    public void setStartProcessDay(int startProcessDay) {
        this.startProcessDay = startProcessDay;
    }

    public int getStartProcessMonth() {
        return startProcessMonth;
    }

    public void setStartProcessMonth(int startProcessMonth) {
        this.startProcessMonth = startProcessMonth;
    }

    public int getEndProcessDay() {
        return endProcessDay;
    }

    public void setEndProcessDay(int endProcessDay) {
        this.endProcessDay = endProcessDay;
    }

    public int getEndProcessMonth() {
        return endProcessMonth;
    }

    public void setEndProcessMonth(int endProcessMonth) {
        this.endProcessMonth = endProcessMonth;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public List<Double> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(List<Double> inputParameters) {
        this.inputParameters = inputParameters;
    }
}
