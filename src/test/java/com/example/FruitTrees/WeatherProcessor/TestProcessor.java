package com.example.FruitTrees.WeatherProcessor;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.MonthlyAndDaily.DailyAndMonthlyWeatherProcessor;

import java.time.LocalDateTime;
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
    public void onStartDay(double value, LocalDateTime date) {
        eventLog.add("StartDay:" + date);
    }

    @Override
    public void onEndDay(Number value, LocalDateTime date) {
        eventLog.add("EndDay:" + date);
    }

    @Override
    protected void onStartNewYear(double value, LocalDateTime date) {
        eventLog.add("StartNewYear:" + date);
    }

    @Override
    protected void onStartNewMonth(double value, LocalDateTime date) {
        eventLog.add("StartNewMonth:" + date);
    }

    @Override
    protected void onMonthEnd(double value, LocalDateTime date) {
        eventLog.add("MonthEnd:" + date);
    }

    @Override
    protected void onEndYear(Number value, LocalDateTime date) {
        eventLog.add("EndYear:" + date);
    }

    @Override
    protected void processWeatherBetween(double value, LocalDateTime date) {
        eventLog.add("Between:" + date);
    }
}