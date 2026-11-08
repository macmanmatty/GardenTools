package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;
import com.example.FruitTrees.Utilities.DataUtilities;

import java.time.LocalDateTime;
import java.util.*;

public class NOAALocationResponse implements LocationResponse {

    /**
     * NOAA weather observations and normalized hourly data.
     */
    private NOAAHourlyDataMap noaaHourlyDataMap =
            new NOAAHourlyDataMap();

    private List<NOAAWeatherRecord> noaaWeatherRecordList =
            new ArrayList<>();

    private Location location;

    private final String DATA_SOURCE = "NOAA";

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public NOAAHourlyDataMap getNoaaHourlyDataMap() {
        return noaaHourlyDataMap;
    }

    public void setNoaaHourlyDataMap(
            NOAAHourlyDataMap noaaHourlyDataMap) {

        this.noaaHourlyDataMap = noaaHourlyDataMap;
    }

    /**
     * Return the normalized hourly timeline.
     */
    @Override
    public LocalDateTime[] getTime() {

        return noaaHourlyDataMap.getNormalizedTime();
    }



    /**
     * Return all normalized data using Open-Meteo datatype names,
     * as expected by the weather processors.
     */
    @Override
    public Map<String, double[]> getData() {

        Map<String, double[]> result = new LinkedHashMap<>();

        if (noaaHourlyDataMap == null ||
                noaaHourlyDataMap.getNormalizedData() == null) {

            return result;
        }

        for (Map.Entry<String, double[]> entry :
                noaaHourlyDataMap.getNormalizedData().entrySet()) {

            String openMeteoType =
                    DataUtilities.toOpenMeteoDatatype(
                            entry.getKey().toLowerCase(Locale.ROOT)
                    );

            result.put(openMeteoType, entry.getValue());
        }

        return result;
    }

    @Override
    public String getDataSource() {
        return DATA_SOURCE;
    }

    public List<NOAAWeatherRecord> getNoaaWeatherRecordList() {
        return noaaWeatherRecordList;
    }

    public void setNoaaWeatherRecordList(
            List<NOAAWeatherRecord> noaaWeatherRecordList) {

        this.noaaWeatherRecordList = noaaWeatherRecordList;
    }
}