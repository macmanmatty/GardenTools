package com.example.FruitTrees.NOAA;

import java.util.*;
import java.util.stream.Collectors;

public class NOAAHourlyDataMap {

    private Map<String, List<NOAAWeatherRecord>> noaaHourlyObservationsMap = new HashMap<>();
    private List <String> time= new ArrayList<>();
    public void addRecords(List<NOAAWeatherRecord> records) {

        records.sort(
                Comparator.comparing(
                        NOAAWeatherRecord::getDate,
                        Comparator.nullsLast(Comparator.naturalOrder())
                )
        );

        time = records.stream()
                .map(NOAAWeatherRecord::getDate)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        noaaHourlyObservationsMap = records.stream()
                .filter(record -> record.getDatatype() != null)
                .collect(Collectors.groupingBy(
                        NOAAWeatherRecord::getDatatype
                ));
    }

    public Map<String, List<NOAAWeatherRecord>> getNoaaHourlyObservationsMap() {
        return noaaHourlyObservationsMap;
    }

    public List<String> getTime() {
        return time;
    }
}
