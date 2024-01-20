package com.example.FruitTrees.OpenMeteo;

import java.util.ArrayList;
import java.util.List;

public class OpenMeteoResponse {

    private double latitude;
    private double longitude;
    private double generationTimeMs;
    private int utcOffsetSeconds;
    private String timezone;
    private String timezoneAbbreviation;
    private double elevation;
    private HourlyUnits hourlyUnits;
    private HourlyData hourly;
    private DailyUnits dailyUnits;
    private DailyData daily;

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

    public double getGenerationTimeMs() {
        return generationTimeMs;
    }

    public void setGenerationTimeMs(double generationTimeMs) {
        this.generationTimeMs = generationTimeMs;
    }

    public int getUtcOffsetSeconds() {
        return utcOffsetSeconds;
    }

    public void setUtcOffsetSeconds(int utcOffsetSeconds) {
        this.utcOffsetSeconds = utcOffsetSeconds;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezoneAbbreviation() {
        return timezoneAbbreviation;
    }

    public void setTimezoneAbbreviation(String timezoneAbbreviation) {
        this.timezoneAbbreviation = timezoneAbbreviation;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public HourlyUnits getHourlyUnits() {
        return hourlyUnits;
    }

    public void setHourlyUnits(HourlyUnits hourlyUnits) {
        this.hourlyUnits = hourlyUnits;
    }

    public HourlyData getHourly() {
        return hourly;
    }

    public void setHourly(HourlyData hourly) {
        this.hourly = hourly;
    }

    public DailyUnits getDailyUnits() {
        return dailyUnits;
    }

    public void setDailyUnits(DailyUnits dailyUnits) {
        this.dailyUnits = dailyUnits;
    }

    public DailyData getDaily() {
        return daily;
    }

    public void setDaily(DailyData daily) {
        this.daily = daily;
    }

    // Getter and setter methods

    // Inner classes for nested structures
    public static class HourlyUnits {
        private String time;
        private String temperature2m;
        private String relativeHumidity2m;
        private String dewPoint2m;
        private String apparentTemperature;
        private String precipitation;
        private String rain;
        private String snowfall;
        private String snowDepth;
        private String weatherCode;
        private String pressureMsl;
        private String surfacePressure;
        private String cloudCover;
        private String cloudCoverLow;
        private String cloudCoverMid;
        private String cloudCoverHigh;
        private String et0FaoEvapotranspiration;
        private String vapourPressureDeficit;
        private String windSpeed10m;
        private String windSpeed100m;
        private String windDirection10m;
        private String windDirection100m;
        private String windGusts10m;
        private String soilTemperature0To7cm;
        private String soilTemperature7To28cm;
        private String soilTemperature28To100cm;
        private String soilTemperature100To255cm;
        private String soilMoisture0To7cm;
        private String soilMoisture7To28cm;
        private String soilMoisture28To100cm;
        private String soilMoisture100To255cm;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTemperature2m() {
            return temperature2m;
        }

        public void setTemperature2m(String temperature2m) {
            this.temperature2m = temperature2m;
        }

        public String getRelativeHumidity2m() {
            return relativeHumidity2m;
        }

        public void setRelativeHumidity2m(String relativeHumidity2m) {
            this.relativeHumidity2m = relativeHumidity2m;
        }

        public String getDewPoint2m() {
            return dewPoint2m;
        }

        public void setDewPoint2m(String dewPoint2m) {
            this.dewPoint2m = dewPoint2m;
        }

        public String getApparentTemperature() {
            return apparentTemperature;
        }

        public void setApparentTemperature(String apparentTemperature) {
            this.apparentTemperature = apparentTemperature;
        }

        public String getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(String precipitation) {
            this.precipitation = precipitation;
        }

        public String getRain() {
            return rain;
        }

        public void setRain(String rain) {
            this.rain = rain;
        }

        public String getSnowfall() {
            return snowfall;
        }

        public void setSnowfall(String snowfall) {
            this.snowfall = snowfall;
        }

        public String getSnowDepth() {
            return snowDepth;
        }

        public void setSnowDepth(String snowDepth) {
            this.snowDepth = snowDepth;
        }

        public String getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(String weatherCode) {
            this.weatherCode = weatherCode;
        }

        public String getPressureMsl() {
            return pressureMsl;
        }

        public void setPressureMsl(String pressureMsl) {
            this.pressureMsl = pressureMsl;
        }

        public String getSurfacePressure() {
            return surfacePressure;
        }

        public void setSurfacePressure(String surfacePressure) {
            this.surfacePressure = surfacePressure;
        }

        public String getCloudCover() {
            return cloudCover;
        }

        public void setCloudCover(String cloudCover) {
            this.cloudCover = cloudCover;
        }

        public String getCloudCoverLow() {
            return cloudCoverLow;
        }

        public void setCloudCoverLow(String cloudCoverLow) {
            this.cloudCoverLow = cloudCoverLow;
        }

        public String getCloudCoverMid() {
            return cloudCoverMid;
        }

        public void setCloudCoverMid(String cloudCoverMid) {
            this.cloudCoverMid = cloudCoverMid;
        }

        public String getCloudCoverHigh() {
            return cloudCoverHigh;
        }

        public void setCloudCoverHigh(String cloudCoverHigh) {
            this.cloudCoverHigh = cloudCoverHigh;
        }

        public String getEt0FaoEvapotranspiration() {
            return et0FaoEvapotranspiration;
        }

        public void setEt0FaoEvapotranspiration(String et0FaoEvapotranspiration) {
            this.et0FaoEvapotranspiration = et0FaoEvapotranspiration;
        }

        public String getVapourPressureDeficit() {
            return vapourPressureDeficit;
        }

        public void setVapourPressureDeficit(String vapourPressureDeficit) {
            this.vapourPressureDeficit = vapourPressureDeficit;
        }

        public String getWindSpeed10m() {
            return windSpeed10m;
        }

        public void setWindSpeed10m(String windSpeed10m) {
            this.windSpeed10m = windSpeed10m;
        }

        public String getWindSpeed100m() {
            return windSpeed100m;
        }

        public void setWindSpeed100m(String windSpeed100m) {
            this.windSpeed100m = windSpeed100m;
        }

        public String getWindDirection10m() {
            return windDirection10m;
        }

        public void setWindDirection10m(String windDirection10m) {
            this.windDirection10m = windDirection10m;
        }

        public String getWindDirection100m() {
            return windDirection100m;
        }

        public void setWindDirection100m(String windDirection100m) {
            this.windDirection100m = windDirection100m;
        }

        public String getWindGusts10m() {
            return windGusts10m;
        }

        public void setWindGusts10m(String windGusts10m) {
            this.windGusts10m = windGusts10m;
        }

        public String getSoilTemperature0To7cm() {
            return soilTemperature0To7cm;
        }

        public void setSoilTemperature0To7cm(String soilTemperature0To7cm) {
            this.soilTemperature0To7cm = soilTemperature0To7cm;
        }

        public String getSoilTemperature7To28cm() {
            return soilTemperature7To28cm;
        }

        public void setSoilTemperature7To28cm(String soilTemperature7To28cm) {
            this.soilTemperature7To28cm = soilTemperature7To28cm;
        }

        public String getSoilTemperature28To100cm() {
            return soilTemperature28To100cm;
        }

        public void setSoilTemperature28To100cm(String soilTemperature28To100cm) {
            this.soilTemperature28To100cm = soilTemperature28To100cm;
        }

        public String getSoilTemperature100To255cm() {
            return soilTemperature100To255cm;
        }

        public void setSoilTemperature100To255cm(String soilTemperature100To255cm) {
            this.soilTemperature100To255cm = soilTemperature100To255cm;
        }

        public String getSoilMoisture0To7cm() {
            return soilMoisture0To7cm;
        }

        public void setSoilMoisture0To7cm(String soilMoisture0To7cm) {
            this.soilMoisture0To7cm = soilMoisture0To7cm;
        }

        public String getSoilMoisture7To28cm() {
            return soilMoisture7To28cm;
        }

        public void setSoilMoisture7To28cm(String soilMoisture7To28cm) {
            this.soilMoisture7To28cm = soilMoisture7To28cm;
        }

        public String getSoilMoisture28To100cm() {
            return soilMoisture28To100cm;
        }

        public void setSoilMoisture28To100cm(String soilMoisture28To100cm) {
            this.soilMoisture28To100cm = soilMoisture28To100cm;
        }

        public String getSoilMoisture100To255cm() {
            return soilMoisture100To255cm;
        }

        public void setSoilMoisture100To255cm(String soilMoisture100To255cm) {
            this.soilMoisture100To255cm = soilMoisture100To255cm;
        }
        // Getter and setter methods
    }

    public static class HourlyData {
        private List<String> time;
        private List<Double> temperature2m;
        private List<Integer> relativeHumidity2m;
        private List<Double> dewPoint2m;
        private List<Double> apparentTemperature;
        private List<Double> precipitation;
        private List<Double> rain;
        private List<Double> snowfall;
        private List<Double> snowDepth;
        private List<Integer> weatherCode;
        private List<Double> pressureMsl;
        private List<Double> surfacePressure;
        private List<Integer> cloudCover;
        private List<Integer> cloudCoverLow;
        private List<Integer> cloudCoverMid;
        private List<Integer> cloudCoverHigh;
        private List<Double> et0FaoEvapotranspiration;
        private List<Double> vapourPressureDeficit;
        private List<Double> windSpeed10m;
        private List<Double> windSpeed100m;
        private List<Integer> windDirection10m;
        private List<Integer> windDirection100m;
        private List<Double> windGusts10m;
        private List<Double> soilTemperature0To7cm;
        private List<Double> soilTemperature7To28cm;
        private List<Double> soilTemperature28To100cm;
        private List<Double> soilTemperature100To255cm;
        private List<Double> soilMoisture0To7cm;
        private List<Double> soilMoisture7To28cm;
        private List<Double> soilMoisture28To100cm;
        private List<Double> soilMoisture100To255cm;

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Double> getTemperature2m() {
            return temperature2m;
        }

        public void setTemperature2m(List<Double> temperature2m) {
            this.temperature2m = temperature2m;
        }

        public List<Integer> getRelativeHumidity2m() {
            return relativeHumidity2m;
        }

        public void setRelativeHumidity2m(List<Integer> relativeHumidity2m) {
            this.relativeHumidity2m = relativeHumidity2m;
        }

        public List<Double> getDewPoint2m() {
            return dewPoint2m;
        }

        public void setDewPoint2m(List<Double> dewPoint2m) {
            this.dewPoint2m = dewPoint2m;
        }

        public List<Double> getApparentTemperature() {
            return apparentTemperature;
        }

        public void setApparentTemperature(List<Double> apparentTemperature) {
            this.apparentTemperature = apparentTemperature;
        }

        public List<Double> getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(List<Double> precipitation) {
            this.precipitation = precipitation;
        }

        public List<Double> getRain() {
            return rain;
        }

        public void setRain(List<Double> rain) {
            this.rain = rain;
        }

        public List<Double> getSnowfall() {
            return snowfall;
        }

        public void setSnowfall(List<Double> snowfall) {
            this.snowfall = snowfall;
        }

        public List<Double> getSnowDepth() {
            return snowDepth;
        }

        public void setSnowDepth(List<Double> snowDepth) {
            this.snowDepth = snowDepth;
        }

        public List<Integer> getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(List<Integer> weatherCode) {
            this.weatherCode = weatherCode;
        }

        public List<Double> getPressureMsl() {
            return pressureMsl;
        }

        public void setPressureMsl(List<Double> pressureMsl) {
            this.pressureMsl = pressureMsl;
        }

        public List<Double> getSurfacePressure() {
            return surfacePressure;
        }

        public void setSurfacePressure(List<Double> surfacePressure) {
            this.surfacePressure = surfacePressure;
        }

        public List<Integer> getCloudCover() {
            return cloudCover;
        }

        public void setCloudCover(List<Integer> cloudCover) {
            this.cloudCover = cloudCover;
        }

        public List<Integer> getCloudCoverLow() {
            return cloudCoverLow;
        }

        public void setCloudCoverLow(List<Integer> cloudCoverLow) {
            this.cloudCoverLow = cloudCoverLow;
        }

        public List<Integer> getCloudCoverMid() {
            return cloudCoverMid;
        }

        public void setCloudCoverMid(List<Integer> cloudCoverMid) {
            this.cloudCoverMid = cloudCoverMid;
        }

        public List<Integer> getCloudCoverHigh() {
            return cloudCoverHigh;
        }

        public void setCloudCoverHigh(List<Integer> cloudCoverHigh) {
            this.cloudCoverHigh = cloudCoverHigh;
        }

        public List<Double> getEt0FaoEvapotranspiration() {
            return et0FaoEvapotranspiration;
        }

        public void setEt0FaoEvapotranspiration(List<Double> et0FaoEvapotranspiration) {
            this.et0FaoEvapotranspiration = et0FaoEvapotranspiration;
        }

        public List<Double> getVapourPressureDeficit() {
            return vapourPressureDeficit;
        }

        public void setVapourPressureDeficit(List<Double> vapourPressureDeficit) {
            this.vapourPressureDeficit = vapourPressureDeficit;
        }

        public List<Double> getWindSpeed10m() {
            return windSpeed10m;
        }

        public void setWindSpeed10m(List<Double> windSpeed10m) {
            this.windSpeed10m = windSpeed10m;
        }

        public List<Double> getWindSpeed100m() {
            return windSpeed100m;
        }

        public void setWindSpeed100m(List<Double> windSpeed100m) {
            this.windSpeed100m = windSpeed100m;
        }

        public List<Integer> getWindDirection10m() {
            return windDirection10m;
        }

        public void setWindDirection10m(List<Integer> windDirection10m) {
            this.windDirection10m = windDirection10m;
        }

        public List<Integer> getWindDirection100m() {
            return windDirection100m;
        }

        public void setWindDirection100m(List<Integer> windDirection100m) {
            this.windDirection100m = windDirection100m;
        }

        public List<Double> getWindGusts10m() {
            return windGusts10m;
        }

        public void setWindGusts10m(List<Double> windGusts10m) {
            this.windGusts10m = windGusts10m;
        }

        public List<Double> getSoilTemperature0To7cm() {
            return soilTemperature0To7cm;
        }

        public void setSoilTemperature0To7cm(List<Double> soilTemperature0To7cm) {
            this.soilTemperature0To7cm = soilTemperature0To7cm;
        }

        public List<Double> getSoilTemperature7To28cm() {
            return soilTemperature7To28cm;
        }

        public void setSoilTemperature7To28cm(List<Double> soilTemperature7To28cm) {
            this.soilTemperature7To28cm = soilTemperature7To28cm;
        }

        public List<Double> getSoilTemperature28To100cm() {
            return soilTemperature28To100cm;
        }

        public void setSoilTemperature28To100cm(List<Double> soilTemperature28To100cm) {
            this.soilTemperature28To100cm = soilTemperature28To100cm;
        }

        public List<Double> getSoilTemperature100To255cm() {
            return soilTemperature100To255cm;
        }

        public void setSoilTemperature100To255cm(List<Double> soilTemperature100To255cm) {
            this.soilTemperature100To255cm = soilTemperature100To255cm;
        }

        public List<Double> getSoilMoisture0To7cm() {
            return soilMoisture0To7cm;
        }

        public void setSoilMoisture0To7cm(List<Double> soilMoisture0To7cm) {
            this.soilMoisture0To7cm = soilMoisture0To7cm;
        }

        public List<Double> getSoilMoisture7To28cm() {
            return soilMoisture7To28cm;
        }

        public void setSoilMoisture7To28cm(List<Double> soilMoisture7To28cm) {
            this.soilMoisture7To28cm = soilMoisture7To28cm;
        }

        public List<Double> getSoilMoisture28To100cm() {
            return soilMoisture28To100cm;
        }

        public void setSoilMoisture28To100cm(List<Double> soilMoisture28To100cm) {
            this.soilMoisture28To100cm = soilMoisture28To100cm;
        }

        public List<Double> getSoilMoisture100To255cm() {
            return soilMoisture100To255cm;
        }

        public void setSoilMoisture100To255cm(List<Double> soilMoisture100To255cm) {
            this.soilMoisture100To255cm = soilMoisture100To255cm;
        }
// Getter and setter methods
    }

    public static class DailyUnits {
        private String time;
        private String weatherCode;
        private String temperature2mMax;
        private String temperature2mMin;
        private String temperature2mMean;
        private String apparentTemperatureMax;
        private String apparentTemperatureMin;
        private String apparentTemperatureMean;
        private String sunrise;
        private String sunset;
        private String daylightDuration;
        private String sunshineDuration;
        private String precipitationSum;
        private String rainSum;
        private String snowfallSum;
        private String precipitationHours;
        private String windSpeed10mMax;
        private String windGusts10mMax;
        private String windDirection10mDominant;
        private String shortwaveRadiationSum;
        private String et0FaoEvapotranspiration;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(String weatherCode) {
            this.weatherCode = weatherCode;
        }

        public String getTemperature2mMax() {
            return temperature2mMax;
        }

        public void setTemperature2mMax(String temperature2mMax) {
            this.temperature2mMax = temperature2mMax;
        }

        public String getTemperature2mMin() {
            return temperature2mMin;
        }

        public void setTemperature2mMin(String temperature2mMin) {
            this.temperature2mMin = temperature2mMin;
        }

        public String getTemperature2mMean() {
            return temperature2mMean;
        }

        public void setTemperature2mMean(String temperature2mMean) {
            this.temperature2mMean = temperature2mMean;
        }

        public String getApparentTemperatureMax() {
            return apparentTemperatureMax;
        }

        public void setApparentTemperatureMax(String apparentTemperatureMax) {
            this.apparentTemperatureMax = apparentTemperatureMax;
        }

        public String getApparentTemperatureMin() {
            return apparentTemperatureMin;
        }

        public void setApparentTemperatureMin(String apparentTemperatureMin) {
            this.apparentTemperatureMin = apparentTemperatureMin;
        }

        public String getApparentTemperatureMean() {
            return apparentTemperatureMean;
        }

        public void setApparentTemperatureMean(String apparentTemperatureMean) {
            this.apparentTemperatureMean = apparentTemperatureMean;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getDaylightDuration() {
            return daylightDuration;
        }

        public void setDaylightDuration(String daylightDuration) {
            this.daylightDuration = daylightDuration;
        }

        public String getSunshineDuration() {
            return sunshineDuration;
        }

        public void setSunshineDuration(String sunshineDuration) {
            this.sunshineDuration = sunshineDuration;
        }

        public String getPrecipitationSum() {
            return precipitationSum;
        }

        public void setPrecipitationSum(String precipitationSum) {
            this.precipitationSum = precipitationSum;
        }

        public String getRainSum() {
            return rainSum;
        }

        public void setRainSum(String rainSum) {
            this.rainSum = rainSum;
        }

        public String getSnowfallSum() {
            return snowfallSum;
        }

        public void setSnowfallSum(String snowfallSum) {
            this.snowfallSum = snowfallSum;
        }

        public String getPrecipitationHours() {
            return precipitationHours;
        }

        public void setPrecipitationHours(String precipitationHours) {
            this.precipitationHours = precipitationHours;
        }

        public String getWindSpeed10mMax() {
            return windSpeed10mMax;
        }

        public void setWindSpeed10mMax(String windSpeed10mMax) {
            this.windSpeed10mMax = windSpeed10mMax;
        }

        public String getWindGusts10mMax() {
            return windGusts10mMax;
        }

        public void setWindGusts10mMax(String windGusts10mMax) {
            this.windGusts10mMax = windGusts10mMax;
        }

        public String getWindDirection10mDominant() {
            return windDirection10mDominant;
        }

        public void setWindDirection10mDominant(String windDirection10mDominant) {
            this.windDirection10mDominant = windDirection10mDominant;
        }

        public String getShortwaveRadiationSum() {
            return shortwaveRadiationSum;
        }

        public void setShortwaveRadiationSum(String shortwaveRadiationSum) {
            this.shortwaveRadiationSum = shortwaveRadiationSum;
        }

        public String getEt0FaoEvapotranspiration() {
            return et0FaoEvapotranspiration;
        }

        public void setEt0FaoEvapotranspiration(String et0FaoEvapotranspiration) {
            this.et0FaoEvapotranspiration = et0FaoEvapotranspiration;
        }
        // Getter and setter methods
    }

    public static class DailyData {
        private List<String> time;
        private List<Integer> weatherCode;
        private List<Double> temperature2mMax;
        private List<Double> temperature2mMin;
        private List<Double> temperature2mMean;
        private List<Double> apparentTemperatureMax;
        private List<Double> apparentTemperatureMin;
        private List<Double> apparentTemperatureMean;
        private List<String> sunrise;
        private List<String> sunset;
        private List<Double> daylightDuration;
        private List<Double> sunshineDuration;
        private List<Double> precipitationSum;
        private List<Double> rainSum;
        private List<Double> snowfallSum;
        private List<Double> precipitationHours;
        private List<Double> windSpeed10mMax;
        private List<Double> windGusts10mMax;
        private List<Integer> windDirection10mDominant;
        private List<Double> shortwaveRadiationSum;
        private List<Double> et0FaoEvapotranspiration;

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Integer> getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(List<Integer> weatherCode) {
            this.weatherCode = weatherCode;
        }

        public List<Double> getTemperature2mMax() {
            return temperature2mMax;
        }

        public void setTemperature2mMax(List<Double> temperature2mMax) {
            this.temperature2mMax = temperature2mMax;
        }

        public List<Double> getTemperature2mMin() {
            return temperature2mMin;
        }

        public void setTemperature2mMin(List<Double> temperature2mMin) {
            this.temperature2mMin = temperature2mMin;
        }

        public List<Double> getTemperature2mMean() {
            return temperature2mMean;
        }

        public void setTemperature2mMean(List<Double> temperature2mMean) {
            this.temperature2mMean = temperature2mMean;
        }

        public List<Double> getApparentTemperatureMax() {
            return apparentTemperatureMax;
        }

        public void setApparentTemperatureMax(List<Double> apparentTemperatureMax) {
            this.apparentTemperatureMax = apparentTemperatureMax;
        }

        public List<Double> getApparentTemperatureMin() {
            return apparentTemperatureMin;
        }

        public void setApparentTemperatureMin(List<Double> apparentTemperatureMin) {
            this.apparentTemperatureMin = apparentTemperatureMin;
        }

        public List<Double> getApparentTemperatureMean() {
            return apparentTemperatureMean;
        }

        public void setApparentTemperatureMean(List<Double> apparentTemperatureMean) {
            this.apparentTemperatureMean = apparentTemperatureMean;
        }

        public List<String> getSunrise() {
            return sunrise;
        }

        public void setSunrise(List<String> sunrise) {
            this.sunrise = sunrise;
        }

        public List<String> getSunset() {
            return sunset;
        }

        public void setSunset(List<String> sunset) {
            this.sunset = sunset;
        }

        public List<Double> getDaylightDuration() {
            return daylightDuration;
        }

        public void setDaylightDuration(List<Double> daylightDuration) {
            this.daylightDuration = daylightDuration;
        }

        public List<Double> getSunshineDuration() {
            return sunshineDuration;
        }

        public void setSunshineDuration(List<Double> sunshineDuration) {
            this.sunshineDuration = sunshineDuration;
        }

        public List<Double> getPrecipitationSum() {
            return precipitationSum;
        }

        public void setPrecipitationSum(List<Double> precipitationSum) {
            this.precipitationSum = precipitationSum;
        }

        public List<Double> getRainSum() {
            return rainSum;
        }

        public void setRainSum(List<Double> rainSum) {
            this.rainSum = rainSum;
        }

        public List<Double> getSnowfallSum() {
            return snowfallSum;
        }

        public void setSnowfallSum(List<Double> snowfallSum) {
            this.snowfallSum = snowfallSum;
        }

        public List<Double> getPrecipitationHours() {
            return precipitationHours;
        }

        public void setPrecipitationHours(List<Double> precipitationHours) {
            this.precipitationHours = precipitationHours;
        }

        public List<Double> getWindSpeed10mMax() {
            return windSpeed10mMax;
        }

        public void setWindSpeed10mMax(List<Double> windSpeed10mMax) {
            this.windSpeed10mMax = windSpeed10mMax;
        }

        public List<Double> getWindGusts10mMax() {
            return windGusts10mMax;
        }

        public void setWindGusts10mMax(List<Double> windGusts10mMax) {
            this.windGusts10mMax = windGusts10mMax;
        }

        public List<Integer> getWindDirection10mDominant() {
            return windDirection10mDominant;
        }

        public void setWindDirection10mDominant(List<Integer> windDirection10mDominant) {
            this.windDirection10mDominant = windDirection10mDominant;
        }

        public List<Double> getShortwaveRadiationSum() {
            return shortwaveRadiationSum;
        }

        public void setShortwaveRadiationSum(List<Double> shortwaveRadiationSum) {
            this.shortwaveRadiationSum = shortwaveRadiationSum;
        }

        public List<Double> getEt0FaoEvapotranspiration() {
            return et0FaoEvapotranspiration;
        }

        public void setEt0FaoEvapotranspiration(List<Double> et0FaoEvapotranspiration) {
            this.et0FaoEvapotranspiration = et0FaoEvapotranspiration;
        }
    }
}