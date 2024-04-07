package com.example.FruitTrees.Location;

import java.math.BigDecimal;

/**
 * class the represents a location
 */
public class Location {
    /**
     * the id of the location
     */
    long id;
    /**
     * the name of the location
     */
    String name;
    /**
     * the latitude and longititude of the location
     */
    String latitude;
    String longitude;
    /**
    the weather station id currently not used
     */
    String stationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
