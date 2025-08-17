package com.example.FruitTrees.WeatherProcessor;

import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class WeatherProcessorFactory {

    private final ApplicationContext context;

    @Autowired
    public WeatherProcessorFactory(ApplicationContext context) {
        this.context = context;
    }

    public WeatherProcessor createProcessor(
                                            HourlyWeatherProcessRequest config,
                                            LocationWeatherResponse locationWeatherResponse) {

        WeatherProcessor processor = context.getBean(config.getProcessorName(),  WeatherProcessor.class);

        // Apply common configuration
        processor.setStartMonthDay(config.getStartProcessMonth(), config.getStartProcessDay());
        processor.setEndMonthDay(config.getEndProcessMonth(), config.getEndProcessDay());
        processor.setDataType(config.getHourlyDataType());
        processor.setLocationWeatherResponse(locationWeatherResponse);
        processor.setCalculateMeanAverage(config.isCalculateMeanAverage());
        processor.setCalculateMedianAverage(config.isCalculateMedianAverage());
        processor.setOnlyCalculateAverage(config.isOnlyCalculateAverage() &&
                (config.isCalculateMeanAverage() ||config.isCalculateMedianAverage()));
        processor.setCalculateMin(config.isCalculateMin());
        processor.setCalculateMax(config.isCalculateMax());
        processor.setUpperBound(config.getUpperBound());
        processor.setLowerBound(config.getLowerBound());
        processor.setThreshold(config.getThreshold());
        processor.setBins(config.getBins());
        processor.setDataTypes(config.getDataTypes());
        return processor;
    }
}