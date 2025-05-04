package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class NOAALocationResponse implements LocationResponse {
  private NOAAHourlyDataMap noaaHourlyDataMap = new NOAAHourlyDataMap();
  private Location location;
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
     * get the data from he map of noaa data as a list of numbers
     * @param type
     * @return
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


}
