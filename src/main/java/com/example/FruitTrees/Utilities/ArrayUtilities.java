package com.example.FruitTrees.Utilities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtilities {


    public static double meanOfList(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }

        double sum = 0.0;
        int count = 0;

        for (double v : numbers) {
            if (Double.isNaN(v)) continue;   // optional but very wise for weather data
            sum += v;
            count++;
        }

        if (count == 0) {
            throw new IllegalArgumentException("List contains no valid numbers");
        }

        return sum / count;
    }

    public static double medianOfList(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }
        List<Double> copy = new ArrayList<>(numbers);
        Collections.sort(copy);

        int n = copy.size();
        if (n % 2 == 0) {
            return (copy.get(n/2 - 1) + copy.get(n/2)) / 2.0;
        }
        return copy.get(n/2);
    }

    public static double minOfList(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }

        double min = Double.POSITIVE_INFINITY;
        for (double v : numbers) {
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    public static double maxOfList(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }

        double max = Double.NEGATIVE_INFINITY;
        for (double v : numbers) {
            if (v > max) {
                max = v;
            }
        }
        return max;
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
