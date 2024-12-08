import './App.css';
import OpenMap from './OpenMap/OpenMap.js';
import LocationWeather from "./Weather/LocationWeather";
import WeatherSettings from "./Weather/WeatherSettings";
import 'bootstrap/dist/css/bootstrap.min.css';
import React from "react";

function App() {
    const weatherRequest = {};  // Initialize an empty object or set up the necessary data

    return (
        <div className="App">
            <OpenMap weatherRequest={weatherRequest} />
            <LocationWeather weatherRequest={weatherRequest} />
            <WeatherSettings weatherRequest={weatherRequest} />
        </div>
    );
}


export default App;
