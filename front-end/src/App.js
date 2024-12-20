import './App.css';
import OpenMap from './OpenMap/OpenMap.js';
import LocationWeather from "./Weather/LocationWeather";
import WeatherSettings from "./Weather/WeatherSettings";
import JsonTextArea from "./Weather/JsonTextArea";

import 'bootstrap/dist/css/bootstrap.min.css';
import React from "react";

function App() {
    const weatherRequest = {};  // Initialize an empty object or set up the necessary data

    return (
        <div className="App">
            <div className="h1"> Weather App</div>
            <OpenMap weatherRequest={weatherRequest}/>
            <WeatherSettings weatherRequest={weatherRequest}/>
            <LocationWeather weatherRequest={weatherRequest}/>
            <JsonTextArea/>
        </div>
    );
}


export default App;
