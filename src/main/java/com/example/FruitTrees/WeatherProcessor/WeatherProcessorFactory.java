package com.example.FruitTrees.WeatherProcessor;

import com.example.FruitTrees.Metrics.Bin;
import com.example.FruitTrees.Metrics.Unit;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculator;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeatherProcessorFactory {
    private static final Logger log = LoggerFactory.getLogger(WeatherProcessorFactory.class);

    private final ApplicationContext context;

    @Autowired
    public WeatherProcessorFactory(ApplicationContext context) {
        this.context = context;
    }

    public WeatherProcessor createHourlyProcessor(
            HourlyWeatherProcessRequest config,
            LocationWeatherResponse locationWeatherResponse, WeatherRequest weatherRequest) {

        WeatherProcessor processor = context.getBean(config.getProcessorName(), WeatherProcessor.class);
        String dataType = DataUtilities.toInternalDatatype(config.getHourlyDataType());
        // unknown datatype exit
        if (dataType == null) {
            log.info("Skipping processor {}: unknown hourlyDataType={}", config.getProcessorName(), config.getHourlyDataType());
            throw new IllegalArgumentException("Unknown hourlyDataType: " + config.getHourlyDataType());
        }
        // Apply common configuration
        processor.setStartMonthDay(config.getStartProcessMonth(), config.getStartProcessDay());
        processor.setEndMonthDay(config.getEndProcessMonth(), config.getEndProcessDay());
        processor.setDataType(dataType);
        processor.setLocationWeatherResponse(locationWeatherResponse);
        processor.setCalculateMeanAverage(config.isCalculateMeanAverage());
        processor.setCalculateMedianAverage(config.isCalculateMedianAverage());
        processor.setOnlyCalculateAverage(config.isOnlyCalculateAverage() &&
                (config.isCalculateMeanAverage() || config.isCalculateMedianAverage()));
        processor.setCalculateMin(config.isCalculateMin());
        processor.setCalculateMax(config.isCalculateMax());
        validateAndSetThresholds(processor, config);
        processor.setDataTypes(config.getDataTypes());
        processor.setCanonicalUnit(config.getUnit());
        if (processor.getOutputUnit() == Unit.SAME_AS_INPUT) {
            processor.setOutputUnit(config.getUnit());
        }
        return processor;
    }

    public void validateAndSetThresholds(WeatherProcessor processor, HourlyWeatherProcessRequest config) {

        Double upper = config.getUpperBound();
        Double lower = config.getLowerBound();
        Double threshold = config.getThreshold();
        List<Bin> bins = config.getBins();

        boolean hasCondition =
                upper != null ||
                        lower != null ||
                        threshold != null ||
                        (bins != null && !bins.isEmpty());

        if (!hasCondition) {
            throw new IllegalArgumentException(
                    "At least one of upperBound, lowerBound, threshold, or bins must be provided"
            );
        }

// normalize BEFORE setting (this is the key move)

// bounds: null = no restriction
        double normalizedLower = (lower != null) ? lower : Double.NEGATIVE_INFINITY;
        double normalizedUpper = (upper != null) ? upper : Double.POSITIVE_INFINITY;

// threshold: depends on how you use it
        double normalizedThreshold = (threshold != null) ? threshold : Double.NaN;

// apply
        processor.setLowerBound(normalizedLower);
        processor.setUpperBound(normalizedUpper);
        processor.setThreshold(normalizedThreshold);

// bins only if present
        if (bins != null && !bins.isEmpty()) {
            processor.setBins(bins);
        }

    }




    public DerivedSeriesCalculator createDerivedSeriesCalculator(String name){

        DerivedSeriesCalculator derivedSeriesCalculator = context.getBean(name,  DerivedSeriesCalculator.class);
        return derivedSeriesCalculator;
    }
}