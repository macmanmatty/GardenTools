import logo from './logo.svg';
import './App.css';
import OpenMap from './OpenMap/OpenMap.js';
import Dates from './Dates.js';
import WeatherOptions from "./CommonUI/OptionDropdown";
import LocationWeather from "./Weather/LocationWeather";
import WeatherSettings from "./Weather/WeatherSettings";

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
