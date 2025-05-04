package com.example.FruitTrees.NOAA;

import java.util.ArrayList;
import java.util.List;

public class NOAAResponse {

  public List<NOAAWeatherRecord> noaaHourlyObservations= new ArrayList<>();
    public List<NOAAWeatherRecord> getNoaaHourlyObservations() {
        return noaaHourlyObservations;
    }

    public void setNoaaHourlyObservations(List<NOAAWeatherRecord> noaaHourlyObservations) {
        this.noaaHourlyObservations = noaaHourlyObservations;
    }


        public  void addValue(NOAAWeatherRecord noaaWeatherRecord) {
        }
}
