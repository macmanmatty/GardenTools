package com.example.FruitTrees.WeatherProcessor.BetweenDates;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates.DateCalculators.LastDateBelowValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LastDateBelowValueTest {

    private LastDateBelowValue processor;
    private LocationWeatherResponse mockLocationWeatherResponse;
    private YearlyValuesResponse mockYearlyValuesResponse;

    @BeforeEach
    void setUp() {
        processor = new LastDateBelowValue();

        // Inject required values
        processor.setInputParameters(List.of("32.0")); // freezing threshold
        processor.setDataType("Temperature");
        processor.setStartMonthDay(9,1);
        processor.setEndMonthDay(12,1);


        // Mock weather response objects
        mockLocationWeatherResponse = mock(LocationWeatherResponse.class);
        mockYearlyValuesResponse = new YearlyValuesResponse();
        mockYearlyValuesResponse.setValues(new HashMap<>());

        when(mockLocationWeatherResponse.getYearlyValues("2023")).thenReturn(mockYearlyValuesResponse);

        processor.setLocationWeatherResponse(mockLocationWeatherResponse);
    }

    @Test
    void testLastDateBelow_found() {
        // Simulate two data points — only the last is below 32.0
        processor.before();
        processor.processWeatherBetween(35.0, "2023-10-10T00:00:00");
        processor.processWeatherBetween(28.5, "2023-10-15T06:00:00");

        processor.onEndDate("2023-12-31");

        // Verify that the value was set
        assertEquals("2023-10-15T06:00", mockYearlyValuesResponse.getValues().get("Last instance of Temperature Below 32.0"));
        assertTrue(processor.getProcessedTextValues().stream().anyMatch(s -> s.contains("2023")));
    }

    @Test
    void testLastDateBelow_neverReached() {
        processor.before();
        processor.processWeatherBetween(40.0, "2023-10-10T00:00:00");
        processor.processWeatherBetween(35.0, "2023-10-15T06:00:00");

        processor.onEndDate("2023-12-31");

        // Verify fallback message
        assertEquals("value never reached", mockYearlyValuesResponse.getValues().get("Last instance of Temperature Below 32.0"));
        assertTrue(processor.getProcessedTextValues().get(0).contains("was never reached"));
    }
}
