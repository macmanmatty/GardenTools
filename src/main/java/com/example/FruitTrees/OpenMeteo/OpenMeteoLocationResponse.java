package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.DataUtilities;

import java.util.List;

public class OpenMeteoLocationResponse  implements LocationResponse {
  private   OpenMeteoResponse openMeteoResponse= new OpenMeteoResponse();
  private Location location;
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
}
