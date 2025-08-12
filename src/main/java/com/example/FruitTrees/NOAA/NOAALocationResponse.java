package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class NOAALocationResponse  {
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


    public LocalDateTime[] getTime() {
        return new LocalDateTime[0];
    }

    /**
     * get the data from the map of noaa data as a list of numbers
     *
     * @param type the data of weather to get
     * @return The list of numerical data
     */
    public double[] getData(String type){
    List<NOAAWeatherRecord> records= noaaHourlyDataMap.getNoaaHourlyObservationsMap().get(type);
            double [] data = records.stream()
                    .map(NOAAWeatherRecord::getValue)
                    .distinct()  // Optional: remove duplicates
                    .sorted().mapToDouble(Number::doubleValue) // unbox + widen
                .toArray();
            return data;
    }

    public String getDataSource() {
        return DATA_SOURCE;
    }
}
