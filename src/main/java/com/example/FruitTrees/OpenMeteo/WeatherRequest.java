package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.ChillingHours.ChillingCalculationMethod;

import java.util.ArrayList;
import java.util.List;

public class WeatherRequest {
   private  List<ChillingCalculationMethod> chillingHoursCalculationMethods = new ArrayList<>();
   private List<String> hourlyDataTypes=new ArrayList<>();
    private List<String> dailyDataTypes=new ArrayList<>();
    private String temperatureUnit;
    private String windSpeedUnit;
    private String precipitationUnit;
    private String timezone;
    private String startDate;
   private String endDate;
   private int startChillMonth;
   private int  startChillDay;
   private int endChillMonth;
   private int  endChillDay;
   private int startPrecipitationMonth;
   private int endPrecipitationMonth;
   private int  endPrecipitationDay;
    private int  startPrecipitationDay;
    private String longitude;
   private String latitude;
   private boolean calculateYearlyChill;
    private boolean calculateYearlyRainFall;
    private boolean calculateYearlySnowFall;

    public List<ChillingCalculationMethod> getChillingHoursCalculationMethods() {
        return chillingHoursCalculationMethods;
    }

    public void setChillingHoursCalculationMethods(List<ChillingCalculationMethod> chillingHoursCalculationMethods) {
        this.chillingHoursCalculationMethods = chillingHoursCalculationMethods;
    }

    public List<String> getHourlyDataTypes() {
        return hourlyDataTypes;
    }

    public void setHourlyDataTypes(List<String> hourlyDataTypes) {
        this.hourlyDataTypes = hourlyDataTypes;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public boolean isCalculateYearlyChill() {
        return calculateYearlyChill;
    }

    public void setCalculateYearlyChill(boolean calculateYearlyChill) {
        this.calculateYearlyChill = calculateYearlyChill;
    }

    public boolean isCalculateYearlyRainFall() {
        return calculateYearlyRainFall;
    }

    public void setCalculateYearlyRainFall(boolean calculateYearlyRainFall) {
        this.calculateYearlyRainFall = calculateYearlyRainFall;
    }

    public boolean isCalculateYearlySnowFall() {
        return calculateYearlySnowFall;
    }

    public void setCalculateYearlySnowFall(boolean calculateYearlySnowFall) {
        this.calculateYearlySnowFall = calculateYearlySnowFall;
    }

    public int getStartPrecipitationMonth() {
        return startPrecipitationMonth;
    }

    public void setStartPrecipitationMonth(int startPrecipitationMonth) {
        this.startPrecipitationMonth = startPrecipitationMonth;
    }

    public int getEndPrecipitationMonth() {
        return endPrecipitationMonth;
    }

    public void setEndPrecipitationMonth(int endPrecipitationMonth) {
        this.endPrecipitationMonth = endPrecipitationMonth;
    }

    public int getEndPrecipitationDay() {
        return endPrecipitationDay;
    }

    public void setEndPrecipitationDay(int endPrecipitationDay) {
        this.endPrecipitationDay = endPrecipitationDay;
    }

    public int getStartPrecipitationDay() {
        return startPrecipitationDay;
    }

    public void setStartPrecipitationDay(int startPrecipitationDay) {
        this.startPrecipitationDay = startPrecipitationDay;
    }

    public int getStartChillMonth() {
        return startChillMonth;
    }

    public void setStartChillMonth(int startChillMonth) {
        this.startChillMonth = startChillMonth;
    }

    public int getStartChillDay() {
        return startChillDay;
    }

    public void setStartChillDay(int startChillDay) {
        this.startChillDay = startChillDay;
    }

    public int getEndChillMonth() {
        return endChillMonth;
    }

    public void setEndChillMonth(int endChillMonth) {
        this.endChillMonth = endChillMonth;
    }

    public int getEndChillDay() {
        return endChillDay;
    }

    public void setEndChillDay(int endChillDay) {
        this.endChillDay = endChillDay;
    }

    public List<String> getDailyDataTypes() {
        return dailyDataTypes;
    }

    public void setDailyDataTypes(List<String> dailyDataTypes) {
        this.dailyDataTypes = dailyDataTypes;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

    public void setPrecipitationUnit(String precipitationUnit) {
        this.precipitationUnit = precipitationUnit;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }



}
