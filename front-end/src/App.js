import './App.css';
import OpenMap from './OpenMap/OpenMap.js';
import LocationWeather from "./Weather/LocationWeather";
import WeatherSettings from "./Weather/WeatherSettings";
import React from "react";

function App() {
  return (
    <div className="App">
        <OpenMap>
        </OpenMap>
        <WeatherSettings/>
           
        <LocationWeather>
        </LocationWeather>

    </div>
  );
}

export default App;
