package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoLocationResponse  implements LocationResponse {
    /**
     * the open meteo weather data sets
     */
  private   OpenMeteoResponse openMeteoResponse= new OpenMeteoResponse();
  private Location location;
  private final String DATA_SOURCE="Open-Meteo";

    public OpenMeteoResponse getOpenMeteoResponse() {
        return openMeteoResponse;
    }

    public void setOpenMeteoResponse(OpenMeteoResponse openMeteoResponse) {
        this.openMeteoResponse = openMeteoResponse;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public List<String> getTime() {
        return openMeteoResponse.hourly.time;
    }

    @Override
    public List<? extends Number> getData(String type){
       return DataUtilities.getHourlyData(openMeteoResponse, type);
    }
    @Override
    public String getDataSource() {
        return DATA_SOURCE;
    }
}
