
package com.example.FruitTrees.NOAA;

public class NOAAStation {

    private final String stationId;
    private final double latitude;
    private final double longitude;
    private final Double elevationMeters;
    private final double distanceMiles;
    private final int yearsAvailable;

    public NOAAStation(
            String stationId,
            double latitude,
            double longitude,
            Double elevationMeters,
            double distanceMiles,
            int yearsAvailable) {

        this.stationId = stationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevationMeters = elevationMeters;
        this.distanceMiles = distanceMiles;
        this.yearsAvailable = yearsAvailable;
    }

    public String getStationId() {
        return stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Double getElevationMeters() {
        return elevationMeters;
    }

    public double getDistanceMiles() {
        return distanceMiles;
    }

    public int getYearsAvailable() {
        return yearsAvailable;
    }

    public Double getElevationDifferenceMeters(
            Double locationElevationMeters) {

        if (elevationMeters == null
                || locationElevationMeters == null) {
            return null;
        }

        return Math.abs(
                elevationMeters - locationElevationMeters
        );
    }
}
