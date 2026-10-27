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
     * Return normalized values for a datatype.
     *
     * Accepts either NOAA datatype names (TMP, DEW)
     * or Open-Meteo datatype names (temperature_2m, dew_point_2m).
     */
    public double[] getData(String type) {

        Map<String, double[]> data =
                noaaHourlyDataMap.getNormalizedData();

        if (data == null || type == null) {
            return new double[0];
        }

        // Direct NOAA datatype lookup.
        double[] values = data.get(type.toUpperCase(Locale.ROOT));

        if (values != null) {
            return values;
        }

        // Look up the corresponding Open-Meteo datatype.
        for (Map.Entry<String, double[]> entry : data.entrySet()) {

            String openMeteoType =
                    DataUtilities.toOpenMeteoDatatype(
                            entry.getKey().toLowerCase(Locale.ROOT)
                    );

            if (type.equalsIgnoreCase(openMeteoType)) {
                return entry.getValue();
            }
        }

        return new double[0];
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