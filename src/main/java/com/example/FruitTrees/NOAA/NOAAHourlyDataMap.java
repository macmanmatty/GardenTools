package com.example.FruitTrees.NOAA;

import java.util.*;
import java.util.stream.Collectors;

public class NOAAHourlyDataMap {

    private Map<String, List<NOAAWeatherRecord>> noaaHourlyObservationsMap = new HashMap<>();
    private List <String> time= new ArrayList<>();
    public void addRecords(List<NOAAWeatherRecord> records) {
       time = records.stream()
                .map(NOAAWeatherRecord::getDate)
                .distinct()  // Optional: remove duplicates
                .sorted()    // Optional: ensure chronological order
                .collect(Collectors.toList());
        records.sort(Comparator.comparing(NOAAWeatherRecord::getDate));
        noaaHourlyObservationsMap= records.stream()
                .collect(Collectors.groupingBy(NOAAWeatherRecord::getDatatype));
    }


    public Map<String, List<NOAAWeatherRecord>> getNoaaHourlyObservationsMap() {
        return noaaHourlyObservationsMap;
    }

    public List<String> getTime() {
        return time;
    }
}
