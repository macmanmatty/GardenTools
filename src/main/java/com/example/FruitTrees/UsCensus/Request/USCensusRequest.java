package com.example.FruitTrees.UsCensus.Request;

import com.example.FruitTrees.Location.Location;

import java.util.ArrayList;
import java.util.List;

public class USCensusRequest {



    private  String year="2020";
   private Location location;
   private List<String> dataFields= new ArrayList<>();
   private String county="*";

    public USCensusRequest() {
        dataFields.add("B01003_001E");
    }
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getDataFields() {
        return dataFields;
    }

    public void setDataFields(List<String> dataFields) {
        this.dataFields = dataFields;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
