package com.example.FruitTrees.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationArea {
    /**
     * the box coordinate of the locations
     */
    List<Coordinate> coordinates= new ArrayList<>();
    private double acres;
    private double sqMeters;
    private   double sqFeet;

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public double getAcres() {
        return acres;
    }

    public void setAcres(double acres) {
        this.acres = acres;
    }

    public double getSqMeters() {
        return sqMeters;
    }

    public void setSqMeters(double sqMeters) {
        this.sqMeters = sqMeters;
    }

    public double getSqFeet() {
        return sqFeet;
    }

    public void setSqFeet(double sqFeet) {
        this.sqFeet = sqFeet;
    }
}
