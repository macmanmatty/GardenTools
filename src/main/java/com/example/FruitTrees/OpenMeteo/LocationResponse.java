package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.Location.Location;

public class LocationResponse {
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
}
