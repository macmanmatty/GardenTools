package com.example.FruitTrees.WeatherProcessor.Daily;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Daily.DaysAboveMonthly;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
public class DaysAboveMonthlyTest {

    @Test
    public void testLastDayOfMonthWith24HoursAboveThreshold() {
        DaysAboveMonthly processor = new DaysAboveMonthly();
        processor.currentYear = 2024;
        processor.getInputParameters().add("32");
        processor.setDataType( "Temperature");
        processor.currentMonthName="FEBRUARY";
        processor.setLocationWeatherResponse(new LocationWeatherResponse());

        processor.before();

        // Simulate Feb 29, 2024 (leap year), 24 hours all above threshold
        for (int hour = 0; hour < 24; hour++) {
            String dateTimeString = String.format("2024-02-29T%02d:00:00", hour);
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);

            processor.processWeather(33.0, dateTime); // value is always above
        }

        // Simulate the start of the next month to trigger onMonthEnd + onStartNewMonth
        processor.processWeather(30.0,           LocalDateTime.parse("2024-03-01T00:00:00"));

        // Check processed values
        String key = "Days Temperature Above 32";
        String summaryText = "Days Temperature Above 32 For FEBRUARY 2024 : 24.0";

        assertEquals("1.0", processor.getLocationWeatherResponse().getYearlyValues("2024").getMonthlyValues("FEBRUARY").getValues().get("Days Temperature Above 32.0"), "Should count 1 full day above 32°F");
    }
}
