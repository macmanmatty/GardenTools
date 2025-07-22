package com.example.FruitTrees.WeatherProcessor.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators.LastDateAboveValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LastDateAboveValueTest {

    private LastDateAboveValue processor;
    private LocationWeatherResponse mockLocationWeatherResponse;
    private YearlyValuesResponse yearlyValues;

    @BeforeEach
    void setUp() {
        processor = new LastDateAboveValue();
        processor.setInputParameters(List.of("80.0")); // freezing threshold
        processor.setDataType("Temperature");
        processor.setStartMonthDay(5,1);
        processor.setEndMonthDay(9,30);


        mockLocationWeatherResponse = mock(LocationWeatherResponse.class);
        yearlyValues = new YearlyValuesResponse();
        yearlyValues.setValues(new HashMap<>());

        when(mockLocationWeatherResponse.getYearlyValues("2023")).thenReturn(yearlyValues);
        processor.setLocationWeatherResponse(mockLocationWeatherResponse);
    }

    @Test
    void testLastDateAbove_found() {
        processor.before();
        processor.processWeatherBetween(75.0, "2023-07-01T14:00:00");
        processor.processWeatherBetween(82.0, "2023-08-01T15:00:00");
        processor.processWeatherBetween(85.0, "2023-08-05T16:00:00");

        processor.onEndDate("2023-09-30");

        String key = "Last instance of Temperature Above 80.0";
        assertEquals("2023-08-05T16:00", yearlyValues.getValues().get(key));
        assertTrue(processor.getProcessedTextValues().stream().anyMatch(s -> s.contains("2023-08-05")));
    }

    @Test
    void testLastDateAbove_neverReached() {
        processor.before();
        processor.processWeatherBetween(60.0, "2023-06-01T12:00:00");
        processor.processWeatherBetween(79.9, "2023-07-10T10:00:00");

        processor.onEndDate("2023-09-30");

        String key = "Last instance of Temperature Above 80.0";
        assertEquals("value never reached", yearlyValues.getValues().get(key));
        assertTrue(processor.getProcessedTextValues().get(0).contains("was never reached"));
    }
}