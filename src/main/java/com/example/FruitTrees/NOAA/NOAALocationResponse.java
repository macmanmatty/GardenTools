package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class NOAALocationResponse implements LocationResponse {
    /**
     * the map of NOAA weather  data
     * key= weather type temperature, precipitation tec.
     * value = list of hourly numeric values
     */
  private NOAAHourlyDataMap noaaHourlyDataMap = new NOAAHourlyDataMap();
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

    @Override
    public List<String> getTime() {
        return noaaHourlyDataMap.getTime();
    }

    /**
     * get the data from the map of noaa data as a list of numbers
     * @param type the data of weather to get
     * @return The list of numerical data
     */
    public List<? extends Number> getData(String type){
    List<NOAAWeatherRecord> records= noaaHourlyDataMap.getNoaaHourlyObservationsMap().get(type);
            List<? extends Number> data = records.stream()
                    .map(NOAAWeatherRecord::getValue)
                    .distinct()  // Optional: remove duplicates
                    .sorted()    // Optional: ensure chronological order
                    .collect(Collectors.toList());
            return data;
    }
    @Override
    public String getDataSource() {
        return DATA_SOURCE;
    }
}
