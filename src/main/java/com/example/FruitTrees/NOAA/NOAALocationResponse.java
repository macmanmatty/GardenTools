package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;
import com.example.FruitTrees.Utilities.DataUtilities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class NOAALocationResponse implements LocationResponse  {
    /**
     * the map of NOAA weather  data
     * key= weather type temperature, precipitation tec.
     * value = list of hourly numeric values
     */
  private NOAAHourlyDataMap noaaHourlyDataMap = new NOAAHourlyDataMap();
  private List<NOAAWeatherRecord> noaaWeatherRecordList = new ArrayList<>();
    /**
     * the location object
     */
  private Location location;
  private final String DATA_SOURCE="NOAA";
  public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public NOAAHourlyDataMap getNoaaHourlyDataMap() {
        return noaaHourlyDataMap;
    }

    public void setNoaaHourlyDataMap(NOAAHourlyDataMap noaaHourlyDataMap) {
        this.noaaHourlyDataMap = noaaHourlyDataMap;
    }


    public LocalDateTime[] getTime() {

        return noaaHourlyDataMap
                .getNoaaHourlyObservationsMap()
                .values()
                .stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(NOAAWeatherRecord::getDate)
                .filter(Objects::nonNull)
                .map(date -> LocalDateTime.ofInstant(
                        Instant.parse(
                                date.endsWith("Z") ? date : date + "Z"
                        ),
                        ZoneOffset.UTC
                ))
                .map(time -> time.truncatedTo(ChronoUnit.HOURS))
                .distinct()
                .sorted()
                .toArray(LocalDateTime[]::new);
    }

    /**
     * get the data from the map of noaa data as a list of numbers
     *
     * @param type the data of weather to get
     * @return The list of numerical data
     */
    public double[] getData(String type) {

        List<NOAAWeatherRecord> records =
                noaaHourlyDataMap
                        .getNoaaHourlyObservationsMap()
                        .get(type);

        if (records == null) {
            return new double[0];
        }

        return records.stream()
                .map(NOAAWeatherRecord::getValue)
                .mapToDouble(Number::doubleValue)
                .toArray();
    }

    @Override
    public Map<String, double[]> getData() {

        Map<String, double[]> result = new LinkedHashMap<>();

        if (noaaHourlyDataMap == null ||
                noaaHourlyDataMap.getNoaaHourlyObservationsMap() == null) {
            return result;
        }

        for (String type : noaaHourlyDataMap
                .getNoaaHourlyObservationsMap()
                .keySet()) {

            result.put(DataUtilities.toOpenMeteoDatatype(type.toLowerCase()), getData(type));
        }

        return result;
    }

    public String getDataSource() {
        return DATA_SOURCE;
    }

    public List<NOAAWeatherRecord> getNoaaWeatherRecordList() {
        return noaaWeatherRecordList;
    }

    public void setNoaaWeatherRecordList(List<NOAAWeatherRecord> noaaWeatherRecordList) {
        this.noaaWeatherRecordList = noaaWeatherRecordList;
    }
}
