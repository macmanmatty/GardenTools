package com.example.FruitTrees.OpenMeteo;

import java.util.ArrayList;
import java.util.List;

public class OpenMeteoLocationResponses {
    /**
     * list  of responses  from the Open Meteo API for given locations
     */
    private List<LocationResponse> locationResponses = new ArrayList<>();

    public List<LocationResponse> getLocationResponses() {
        return locationResponses;
    }

    public void setLocationResponses(List<LocationResponse> locationResponses) {
        this.locationResponses = locationResponses;
    }
}