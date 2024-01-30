import React, {useState} from "react";
import Dates from "../Dates";
import OptionDropDown from "../CommonUI/OptionDropdown";
import * as WeatherOptions from "./WeatherOptions";
import './Weather.css';

const LocationWeather = ({mapLatitude, mapLongitude}) => {
    // State to manage the selected value
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [weatherData, setWeatherData]=useState({});
    const [tempUnits, setTempUnits]=useState('');
    const [windSpeedUnits, setWindSpeedUnits]=useState('');
    const [precipitationUnits, setPrecipitationUnits]=useState('');
    const [weatherDataTypes, setWeatherDataTypes]=useState([]);
    const [timeZone, setTimeZone]=useState([]);
    const [startChillDate, setStartChillDate]=useState([]);
    const [endChillDate, setEndChillDate]=useState([]);
    const addWeatherDataType = (item) => {
        // Create a copy of the current array and add a new item to it
        const updatedItems = [...weatherDataTypes, item];
        // Set the updated array as the new state
        setWeatherDataTypes(updatedItems);
    };

    // Handler function to update the selected value
    const getWeather = (event) => {
        let weatherRequestForm={
            latitude:mapLatitude,
            longitude:mapLongitude,
            temperatureUnit:tempUnits,
            precipitationUnit: precipitationUnits,
            windSpeedUnits: setWindSpeedUnits,
            timezone: timeZone,
            startDate: startDate,
            endDate: endDate,
            startChillMonthAndDay: '',
            endChillMonthAndDay: '',
             startPrecipitationMonth: '',
             endPrecipitationMonth: '',
              endPrecipitationDay: '',
              startPrecipitationDay: '',
             calculateYearlyChill: '',
             calculateYearlyRainFall: '',
             calculateYearlySnowFall: '',

        }
    };
    return (
        <div>
            <div  class='inlineDropdowns'>
            <OptionDropDown
                setSelectedValue={setWeatherData}
                optionsArray={WeatherOptions.weatherOptions}
                displayParameter='name'
                id={'dataType'}
                onSelected={setWeatherData}
                labelText={'Select A Weather Calculation:'}
            ></OptionDropDown>
            </div>
            <Dates
                setStartDate={setStartDate}
                setEndDate={setEndDate}
            />
        </div>

    );
};

export default LocationWeather