package com.example.FruitTrees.WeatherProcessor;

import com.example.FruitTrees.Metrics.MetricDef;
import com.example.FruitTrees.Metrics.MetricRegistry;
import com.example.FruitTrees.Metrics.Unit;
import com.example.FruitTrees.Metrics.Units;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculator;
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
    public WeatherProcessor createHourlyProcessor(
            HourlyWeatherProcessRequest config,
            LocationWeatherResponse locationWeatherResponse) {

        // Create the processor bean (Spring-managed)
        WeatherProcessor processor = context.getBean(config.getProcessorName(), WeatherProcessor.class);

        // Resolve the internal canonical data type (e.g. "temperature_2m")
        String dataType = DataUtilities.toInternalDatatype(config.getHourlyDataType());

        // Unknown or unsupported data type -> nothing to process
        if (dataType == null) {
            return null;
        }

        // Lookup the physical definition (quantity + canonical unit)
        MetricDef metricDef = MetricRegistry.defFor(DataUtilities.toInternalDatatype(dataType));

        // ---------------- Time window configuration ----------------

        processor.setStartMonthDay(config.getStartProcessMonth(), config.getStartProcessDay());
        processor.setEndMonthDay(config.getEndProcessMonth(), config.getEndProcessDay());

        // ---------------- Core metadata ----------------

        processor.setDataType(dataType);
        processor.setLocationWeatherResponse(locationWeatherResponse);

        // ---------------- Statistical options ----------------

        processor.setCalculateMeanAverage(config.isCalculateMeanAverage());
        processor.setCalculateMedianAverage(config.isCalculateMedianAverage());
        processor.setOnlyCalculateAverage(
                config.isOnlyCalculateAverage() &&
                        (config.isCalculateMeanAverage() || config.isCalculateMedianAverage())
        );

        processor.setCalculateMin(config.isCalculateMin());
        processor.setCalculateMax(config.isCalculateMax());

        // ---------------- Unit normalization (CRITICAL PART) ----------------
        //
        // All internal computations must operate in canonical units defined
        // by the MetricRegistry (e.g. °C for temperature, kPa for VPD, m/s for wind).
        // Convert user-supplied thresholds at the boundary and never mix units inside.


        MetricDef def=MetricRegistry.defFor(DataUtilities.toInternalDatatype(dataType));
        // Lower bound
        if (config.getLowerBound() != null) {
            Unit lowerUnit = Unit.fromString(config.getLowerBound().toString());
            double lowerCanonical = Units.convert(
                    metricDef.type(),
                    config.getLowerBound(),
                    lowerUnit,
                    def.canonicalUnit()
            );
            processor.setLowerBound(lowerCanonical);
        }

        // Upper bound
        if (config.getUpperBound() != null) {
            Unit upperUnit = Unit.fromString(config.getUpperBound().toString());
            double upperCanonical = Units.convert(
                    metricDef.type(),
                    config.getUpperBound(),
                    upperUnit,
                    def.canonicalUnit()
            );
            processor.setUpperBound(upperCanonical);
        }

        // Threshold (e.g. hours above X, below X, between X–Y)
        if (config.getThreshold() != null) {
            Unit thresholdUnit = Unit.fromString(config.getThreshold().toString());
            double thresholdCanonical = Units.convert(
                    metricDef.type(),
                    config.getThreshold(),
                    thresholdUnit,
                    def.canonicalUnit()
            );
            processor.setThreshold(thresholdCanonical);
        }

        // ---------------- Histogram / aggregation settings ----------------

        processor.setBins(config.getBins());
        processor.setDataTypes(config.getDataTypes());

        // At this point:
        //   - All temperatures are in °C
        //   - All pressures are in  kPa
        //   - All wind speeds are in m/s
        //   - All depths are in mm
        //
        // The processor can now run pure physics/statistics without ever
        // worrying about whether a number came in as °F, inches, or mph.

        return processor;
    }



    public DerivedSeriesCalculator createDerivedSeriesCalculator(String name){

        DerivedSeriesCalculator derivedSeriesCalculator = context.getBean(name,  DerivedSeriesCalculator.class);
        return derivedSeriesCalculator;
    }
}