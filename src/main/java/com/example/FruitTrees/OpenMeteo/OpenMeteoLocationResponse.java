package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoLocationResponse  implements LocationResponse {
    /**
     * the open meteo weather data sets
     */
  private   OpenMeteoResponse openMeteoResponse= new OpenMeteoResponse();
  private Location location;
  private final String DATA_SOURCE="Open-Meteo";

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

    @Override
    public LocalDateTime[] getTime() {
        DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        return openMeteoResponse.hourly.time.stream()
                .map(s -> LocalDateTime.parse(s, FMT))          // parse without offset
                .map(ldt -> ldt.atZone(ZoneOffset.UTC))         // assume UTC
                .map(ZonedDateTime::toLocalDateTime)            // strip zone if you want pure LocalDateTime
                .toArray(LocalDateTime[]::new);
    }

    @Override
    public Map< String, double[]> getData(){
        return  DataUtilities.getAllHourlyData(openMeteoResponse);
    }
    @Override
    public String getDataSource() {
        return DATA_SOURCE;
    }
}
