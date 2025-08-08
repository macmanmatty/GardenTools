package com.example.FruitTrees.Utilities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtilities {

    public static double  medianOfList(List<Double> numbers){
        double average;
        Collections.sort(numbers); // Step 1: Sort the array
        int n = numbers.size();
        // Step 2: Check if length is even or odd
        if (n % 2 == 0) {
            // Even: average of middle two numbers
            average = (numbers.get(n / 2 - 1) + numbers.get(n / 2)) / 2.0;
        } else {
            // Odd: return the middle number
            average= numbers.get(n / 2);
        }
        return average;
    }

    public static LocalDateTime medianOfDateTimes(List<LocalDateTime> dateTimes) {
        // Convert to epoch seconds (or millis), sort, and get median
        List<Long> epochSeconds = dateTimes.stream()
                .map(dt -> dt.toEpochSecond(ZoneOffset.UTC.UTC)) // You can change to system default if needed
                .sorted()
                .collect(Collectors.toList());

        int n = epochSeconds.size();
        long medianEpoch;
        if (n % 2 == 0) {
            medianEpoch = (epochSeconds.get(n / 2 - 1) + epochSeconds.get(n / 2)) / 2;
        } else {
            medianEpoch = epochSeconds.get(n / 2);
        }

        return LocalDateTime.ofEpochSecond(medianEpoch, 0, ZoneOffset.UTC);
    }

}
