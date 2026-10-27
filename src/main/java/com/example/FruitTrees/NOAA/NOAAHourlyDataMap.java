package com.example.FruitTrees.NOAA;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class NOAAHourlyDataMap {

    private Map<String, List<NOAAWeatherRecord>> noaaHourlyObservationsMap = new HashMap<>();
    private List <String> time= new ArrayList<>();
    private LocalDateTime[] normalizedTime;
    private Map<String, double[]> normalizedData;
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


    public LocalDateTime[] getNormalizedTime() {
        return normalizedTime;
    }

    public Map<String, double[]> getNormalizedData() {
        return normalizedData;
    }

    public Map<String, List<NOAAWeatherRecord>> getNoaaHourlyObservationsMap() {
        return noaaHourlyObservationsMap;
    }

    public void setNormalizedData(LocalDateTime[] normalizedTime, Map<String,double[]> normalizedData) {

    this.normalizedTime = normalizedTime;
    this.normalizedData = normalizedData;
        }

}
