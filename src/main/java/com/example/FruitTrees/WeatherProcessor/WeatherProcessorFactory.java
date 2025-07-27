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
        processor.setInputParameters(config.getInputParameters());
        processor.setDataType(config.getHourlyDataType());
        processor.setLocationWeatherResponse(locationWeatherResponse);
        processor.setCalculateAverage(config.isCalculateAverage());
        processor.setOnlyCalculateAverage(config.isOnlyCalculateAverage() && config.isCalculateAverage());
        processor.setCalculateMin(config.isCalculateMin());
        processor.setCalculateMax(config.isCalculateMax());

        return processor;
    }
}