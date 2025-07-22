package com.example.FruitTrees.WeatherProcessor;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily.DailyAndMonthlyWeatherProcessor;

import java.util.ArrayList;
import java.util.List;

public class TestProcessor extends DailyAndMonthlyWeatherProcessor {
    public final List<String> eventLog = new ArrayList<>();

    public TestProcessor() {
        super("TestProcessor");
        this.dataType = "Temperature";
        locationWeatherResponse= new LocationWeatherResponse();
    }

    @Override
    public void onStartDay(Number value, String date) {
        eventLog.add("StartDay:" + date);
    }

    @Override
    public void onEndDay(Number value, String date) {
        eventLog.add("EndDay:" + date);
    }

    @Override
    protected void onStartNewYear(Number value, String date) {
        eventLog.add("StartNewYear:" + date);
    }

    @Override
    protected void onStartNewMonth(Number value, String date) {
        eventLog.add("StartNewMonth:" + date);
    }

    @Override
    protected void onMonthEnd(Number value, String date) {
        eventLog.add("MonthEnd:" + date);
    }

    @Override
    protected void onEndYear(Number value, String date) {
        eventLog.add("EndYear:" + date);
    }

    @Override
    protected void processWeatherBetween(Number data, String date) {
        eventLog.add("Between:" + date);
    }
}