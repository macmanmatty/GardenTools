package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;

import com.example.FruitTrees.Location.Location;

record WeatherRunContext(
        Location location,              // full object: name, lat, lon, state, county, etc.
        ObservationCollector collector,
        int calcVersion
) {
    public String locationId() {
        return location.id();
    }
}
