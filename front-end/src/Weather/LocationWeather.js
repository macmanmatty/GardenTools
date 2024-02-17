import React, {useState} from "react";
import Dates from "../CommonUI/Dates";
import OptionDropDown from "../CommonUI/OptionDropdown";
import * as WeatherOptions from "./WeatherOptions";
import './Weather.css';

const LocationWeather = ({mapLatitude, mapLongitude}) => {
    // State to manage the selected value
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [tempUnits, setTempUnits]=useState('');
    const [windSpeedUnits, setWindSpeedUnits]=useState('');
    const [precipitationUnits, setPrecipitationUnits]=useState('');
    const [weatherDataTypes, setWeatherDataTypes]=useState([]);
    const [timeZone, setTimeZone]=useState([]);
    const [startChillDate, setStartChillDate]=useState([]);
    const [endChillDate, setEndChillDate]=useState([]);
    const[SelectedCalculationComponent, setSelectedCalculationComponent]=useState(null);

    const addWeatherDataType = (item) => {
        // Create a copy of the current array and add a new item to it
        const updatedItems = [...weatherDataTypes, item];
        // Set the updated array as the new state
        setWeatherDataTypes(updatedItems);
    };
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

    // Handler function to update the selected value
    const getWeather = (event) => {

    };
    return (
        <div>
            <Dates
                startText={"Select a date to start processing Weather on"}
                endText={"Select a date to end processing Weather on"}
                setStartDate={setStartDate}
                setEndDate={setEndDate}
            />
            <div  class='inlineDropdowns'>

            <OptionDropDown
                optionsArray={WeatherOptions.weatherOptions}
                displayParameter='name'
                value='name'
                id={'dataType'}
                onSelected={setSelectedCalculationComponent}
                labelText={'Select A Weather Calculation:'}
            ></OptionDropDown>
            </div>

            {SelectedCalculationComponent ? <SelectedCalculationComponent
                weatherRequest={weatherRequestForm}
            /> : "Please select a  weather value to calculate"}
            <div>
            <button
                onClick={getWeather}
            >
                Get Weather

            </button>
            </div>

        </div>

    );
};

export default LocationWeather