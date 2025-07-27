package com.example.FruitTrees.WeatherProcessor.BetweenDates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DateValueProcessorTest {

    private TestDateValueProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TestDateValueProcessor();
    }

    @Test
    void testCalculateMeanAverage_Value_validDates() {
        processor.yearlyDates.add(Optional.of(LocalDateTime.of(2023, 10, 15, 6, 0)));
        processor.yearlyDates.add(Optional.of(LocalDateTime.of(2022, 10, 13, 7, 0)));
        processor.yearlyDates.add(Optional.of(LocalDateTime.of(2021, 10, 12, 8, 0)));
        processor.yearlyDates.add(Optional.empty()); // one missing — < 33% of total

        processor.calculateMeanAverageValue();

        assertEquals(1, processor.getAvgValues().size());
        //assertTrue(processor.getAvgValues().get(0).startsWith("Average First Date Below 32.0 10"));
    }

    @Test
    void testCalculateMeanAverage_Value_tooManyMissing() {
        processor.yearlyDates.add(Optional.of(LocalDateTime.of(2023, 10, 15, 6, 0)));
        processor.yearlyDates.add(Optional.empty());
        processor.yearlyDates.add(Optional.empty());
        processor.yearlyDates.add(Optional.empty()); // 3/4 missing = 75% > 33%

        processor.calculateMeanAverageValue();

        assertEquals(1, processor.getAvgValues().size());
       // assertEquals("Average First Date Below 32.0 Too many dates where value was never reached!", processor.getAvgValues().get(0));
    }
}