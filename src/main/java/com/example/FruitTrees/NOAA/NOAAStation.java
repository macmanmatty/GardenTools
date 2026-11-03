package com.example.FruitTrees.NOAA;

public class NOAAStation {
    String stationId;
    double latitude;
    double longitude;
    Double elevationMeters;
    double distanceMiles;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getElevationMeters() {
        return elevationMeters;
    }

    public void setElevationMeters(Double elevationMeters) {
        this.elevationMeters = elevationMeters;
    }

    public double getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(double distanceMiles) {
        this.distanceMiles = distanceMiles;
    }

    public Double getElevationDifferenceMeters() {
        return 0.0d;
    }
}

