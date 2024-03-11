import './App.css';
import OpenMap from './OpenMap/OpenMap.js';
import LocationWeather from "./Weather/LocationWeather";
import WeatherSettings from "./Weather/WeatherSettings";
import React from "react";

function WeatherMap() {
    const weatherRequestForm={
        latitude:mapLatitude,
        longitude:mapLongitude,
        temperatureUnit:tempUnits,
        precipitationUnit: precipitationUnits,
        windSpeedUnits: setWindSpeedUnits,
        timezone: timeZone,
        startDate: startDate,
        endDate: endDate,
        hourlyDataTypes: new Set(),
        dailyDataTypes : new Set(),
        hourlyWeatherProcessRequests: []
    }


    return (
        <div className="App">
            <OpenMap
                weatherRequestFrom={weatherRequestForm}
            >
            </OpenMap>
            <WeatherSettings/>
            <LocationWeather>
            </LocationWeather>

        </div>
    );
}

export default WeatherMap;
