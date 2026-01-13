package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Daily;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DaysBetweenMonthlyTest {

    @Test
    public void testLastDayOfMonthWith24HoursBetweenThreshold() {
        DaysBetweenMonthly processor = new DaysBetweenMonthly();
        processor.currentYear = 2024;
        processor.setLowerBound(32);
        processor.setUpperBound(56);
        processor.setDataType( "Temperature");
        processor.currentMonthName="FEBRUARY";
        processor.setLocationWeatherResponse(new LocationWeatherResponse());

        processor.before();

        // Simulate Feb 29, 2024 (leap year), 24 hours all above threshold
        for (int hour = 0; hour < 24; hour++) {
            String dateTimeString = String.format("2024-02-28T%02d:00:00", hour);
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);

            processor.processWeather(33.0, dateTime); // value is always above
        }
        for (int hour = 0; hour < 24; hour++) {
            String dateTimeString = String.format("2024-02-29T%02d:00:00", hour);
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);

            processor.processWeather(57.0, dateTime); // value is always above
        }
        // Simulate the start of the next month to trigger onMonthEnd + onStartNewMonth
        processor.processWeather(30.0,           LocalDateTime.parse("2024-03-01T00:00:00"));

        // Check processed values
        String key = "Days Temperature Above 32";
        String summaryText = "Days Temperature Above 32 For FEBRUARY 2024 : 24.0";
        assertEquals("1.0", processor.getLocationWeatherResponse().getYearlyValues("2024").getMonthlyValues("FEBRUARY").getValues().get("Days Between 32.0 And 56.0 Of Temperature"), "Should count 1 full day above 32°F and below 56 ");
    }
}
