package com.example.FruitTrees.ChillingHours.WeatherProcessors;

public class TotalCalculator implements WeatherProcessor {
    private double value;
    private double maxTemp;
    private double minTemp;
    private int startDay;
    private int  endDay;
    private int startMonth;
    private int  endMonth;

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public void processWeather(double value, String date) {
            this.value=this.value+value;

    }

    public int getStartDay() {
        return startDay;
    }

    @Override
    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getEndDay() {
        return endDay;
    }

    @Override
    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getStartMonth() {
        return startMonth;
    }

    @Override
    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getEndMonth() {
        return endMonth;
    }

    @Override
    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }
}
