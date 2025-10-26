package com.example.FruitTrees.WeatherProcessor.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators.FirstDateBelowValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FirstDateBelowValueTest {

    private FirstDateBelowValue processor;
    private LocationWeatherResponse mockLocationWeatherResponse;
    private YearlyValuesResponse yearlyValues;

    @BeforeEach
    void setUp() {
        processor = new FirstDateBelowValue();
        processor.setThreshold(32.0); // freezing threshold
        processor.setDataType("Temperature");
        processor.setStartMonthDay(9,1);
        processor.setEndMonthDay(12,31);

        mockLocationWeatherResponse = mock(LocationWeatherResponse.class);
        yearlyValues = new YearlyValuesResponse();
        yearlyValues.setValues(new HashMap<>());

        when(mockLocationWeatherResponse.getYearlyValues("2023")).thenReturn(yearlyValues);
        processor.setLocationWeatherResponse(mockLocationWeatherResponse);
    }

    @Test
    void testFirstDateBelow_found() {
        processor.before();
        processor.processWeatherBetween(40.0, LocalDateTime.parse("2023-10-01T08:00:00"));
        processor.processWeatherBetween(29.5, LocalDateTime.parse("2023-10-15T06:00:00"));
        processor.startProcessing();
        String key = "First instance of Temperature Below 32.0";
        assertEquals("2023-10-15T06:00", yearlyValues.getValues().get(key));
        assertTrue(processor.getProcessedTextValues().stream().anyMatch(s -> s.contains("2023-10-15")));
    }

    @Test
    void testFirstDateBelow_neverReached() {
        processor.before();
        processor.processWeather(40.0, LocalDateTime.parse("2023-10-01T08:00:00"));
        processor.processWeather(35.0, LocalDateTime.parse("2023-11-15T12:00:00"));

        processor.onStop(LocalDateTime.parse("2023-12-31T00:00:00"));
        processor.startProcessing();

        String key = "First instance of Temperature Below 32.0";
        assertEquals("value never reached", yearlyValues.getValues().get(key));
        assertTrue(processor.getProcessedTextValues().get(0).contains("was never reached"));
    }
}