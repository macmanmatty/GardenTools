package com.example.FruitTrees.NOAA;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NOAAGlobalHourlyRecord {

    @JsonProperty("DATE")
    private String date;

    @JsonProperty("STATION")
    private String station;

    @JsonProperty("TMP")
    private String temperature;

    @JsonProperty("DEW")
    private String dewPoint;

    @JsonProperty("AA1")
    private String precipitation;

    @JsonProperty("WND")
    private String wind;

    public String getDate() {
        return date;
    }

    public String getStation() {
        return station;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDewPoint() {
        return dewPoint;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public String getWind() {
        return wind;
    }
}