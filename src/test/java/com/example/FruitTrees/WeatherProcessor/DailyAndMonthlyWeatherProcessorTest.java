package com.example.FruitTrees.WeatherProcessor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DailyAndMonthlyWeatherProcessorTest {

    public void testLifecycleHooksOverThreeDays() {
        TestProcessor processor = new TestProcessor();
        processor.currentYear = 2025;

        processor.before();

        String[] sampleDates = {
                "2024-12-31T23:00:00",                  // New Year's Eve
                "2025-01-01T00:00:00", "2025-01-01T01:00:00", "2025-01-01T23:00:00",
                "2025-01-02T00:00:00", "2025-01-02T23:00:00",
                "2025-01-03T00:00:00", "2025-01-03T23:00:00"
        };

        for (String timestamp : sampleDates) {
            LocalDateTime ts=LocalDateTime.parse(timestamp);
            processor.processWeather(32.0, ts);
        }

        List<String> events = processor.eventLog;

        // Validate some key lifecycle events
        assertTrue(events.contains("StartDay:2025-01-01T00:00:00"));
        assertTrue(events.contains("EndDay:2025-01-01T23:00:00"));
        assertTrue(events.contains("StartNewYear:2025-01-01T00:00:00"));
        assertTrue(events.contains("StartNewMonth:2025-01-01T00:00:00"));
        assertTrue(events.contains("EndYear:2024-12-31T23:00:00"));
        assertTrue(events.contains("MonthEnd:2024-12-31T23:00:00"));
        assertTrue(events.contains("MonthEnd:2025-01-30T23:00:00") == false); // should not be present yet

        // Ensure `processWeatherBetween` was called multiple times
        long betweenCalls = events.stream().filter(e -> e.startsWith("Between:")).count();
        assertEquals(sampleDates.length, betweenCalls);
    }
}
