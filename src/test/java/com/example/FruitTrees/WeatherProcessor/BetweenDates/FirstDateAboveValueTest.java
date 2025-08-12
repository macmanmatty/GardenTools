package com.example.FruitTrees.WeatherProcessor.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators.FirstDateAboveValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FirstDateAboveValueTest {

    private FirstDateAboveValue processor;
    private LocationWeatherResponse mockLocationWeatherResponse;
    private YearlyValuesResponse mockYearlyValuesResponse;

    @BeforeEach
    void setup() {
        processor = new FirstDateAboveValue();

        // Required setup values

        processor.setInputParameters(List.of("75.0")); // freezing threshold
        processor.setDataType("Temperature");
        processor.setStartMonthDay(4,1);
        processor.setEndMonthDay(9,30);
        processor.startProcessing();
        // Mock response objects
        mockLocationWeatherResponse = mock(LocationWeatherResponse.class);
        mockYearlyValuesResponse = new YearlyValuesResponse();
        mockYearlyValuesResponse.setValues(new HashMap<>());

        when(mockLocationWeatherResponse.getYearlyValues("2023")).thenReturn(mockYearlyValuesResponse);
        processor.setLocationWeatherResponse(mockLocationWeatherResponse);
    }

    @Test
    void testFirstDateAbove_found() {
        processor.before();

        // Value below threshold
        processor.processWeather(70.0, LocalDateTime.parse("2023-05-01T12:00:00"));
        // Value above threshold — should trigger termination
        processor.processWeather(77.0, LocalDateTime.parse("2023-05-03T14:00:00"));
        processor.processWeather(77.0, LocalDateTime.parse("2023-05-22T14:00:00"));
        processor.startProcessing();

        // Check stored value
        String key = "First instance of Temperature Above 75.0";
        assertEquals("2023-05-03T14:00", mockYearlyValuesResponse.getValues().get(key));
        assertTrue(processor.getProcessedTextValues().stream().anyMatch(s -> s.contains("2023-05-03")));
    }

    @Test
    void testFirstDateAbove_neverReached() {
        processor.before();

        // All values below threshold
        processor.processWeather(68.0, LocalDateTime.parse("2023-05-01T12:00:00"));
        processor.processWeather(70.0, LocalDateTime.parse("2023-06-01T14:00:00"));

        processor.onStop(LocalDateTime.parse("2023-12-31T00:00:00"));

        String key = "First instance of Temperature Above 75.0";
        assertEquals("value never reached", mockYearlyValuesResponse.getValues().get(key));
        assertTrue(processor.getProcessedTextValues().get(0).contains("was never reached"));
    }


}