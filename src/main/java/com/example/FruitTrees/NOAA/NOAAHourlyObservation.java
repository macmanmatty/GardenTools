package com.example.FruitTrees.NOAA;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
public class NOAAHourlyObservation{

        @JsonProperty("date")
        public LocalDateTime dateTime;

        @JsonProperty("station")
        public String stationId;

        @JsonProperty("name")
        public String stationName;

        @JsonProperty("latitude")
        public Double latitude;

        @JsonProperty("longitude")
        public Double longitude;

        @JsonProperty("elevation")
        public Double elevation;

        @JsonProperty("temperature")
        public Double temperature;

        @JsonProperty("dewpoint")
        public Double dewPoint;

        @JsonProperty("humidity")
        public Double humidity;

        @JsonProperty("windDirection")
        public Double windDirection;

        @JsonProperty("windSpeed")
        public Double windSpeed;

        @JsonProperty("windGust")
        public Double windGust;

        @JsonProperty("pressure")
        public Double pressure;

        @JsonProperty("precipitation")
        public Double precipitation;

        @JsonProperty("snowDepth")
        public Double snowDepth;

        @JsonProperty("visibility")
        public Double visibility;

        @JsonProperty("cloudCover")
        public String cloudCover;

        @JsonProperty("weatherType")
        public String weatherType;

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getElevation() {
            return elevation;
        }

        public void setElevation(Double elevation) {
            this.elevation = elevation;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Double getDewPoint() {
            return dewPoint;
        }

        public void setDewPoint(Double dewPoint) {
            this.dewPoint = dewPoint;
        }

        public Double getHumidity() {
            return humidity;
        }

        public void setHumidity(Double humidity) {
            this.humidity = humidity;
        }

        public Double getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(Double windDirection) {
            this.windDirection = windDirection;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(Double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public Double getWindGust() {
            return windGust;
        }

        public void setWindGust(Double windGust) {
            this.windGust = windGust;
        }

        public Double getPressure() {
            return pressure;
        }

        public void setPressure(Double pressure) {
            this.pressure = pressure;
        }

        public Double getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(Double precipitation) {
            this.precipitation = precipitation;
        }

        public Double getSnowDepth() {
            return snowDepth;
        }

        public void setSnowDepth(Double snowDepth) {
            this.snowDepth = snowDepth;
        }

        public Double getVisibility() {
            return visibility;
        }

        public void setVisibility(Double visibility) {
            this.visibility = visibility;
        }

        public String getCloudCover() {
            return cloudCover;
        }

        public void setCloudCover(String cloudCover) {
            this.cloudCover = cloudCover;
        }

        public String getWeatherType() {
            return weatherType;
        }

        public void setWeatherType(String weatherType) {
            this.weatherType = weatherType;
        }
    }