import React, {useState} from "react";
import Dates from "../CommonUI/Dates";
import OptionDropDown from "../CommonUI/OptionDropdown";
import * as WeatherOptions from "./WeatherOptions";

const LocationWeather = ({mapLatitude, mapLongitude}) => {
    // State to manage the selected value
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [tempUnits, setTempUnits]=useState('');
    const [windSpeedUnits, setWindSpeedUnits]=useState('');
    const [precipitationUnits, setPrecipitationUnits]=useState('');
    const [weatherDataTypes, setWeatherDataTypes]=useState([]);
    const [timeZone, setTimeZone]=useState([]);
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
            hourlyWeatherProcessRequests: []

        }
    };
    return (
        <div>
            <div  class='inlineDropdowns'>
            <OptionDropDown
                optionsArray={WeatherOptions.temperatureUnits}
                displayParameter='key'
                value='key'
                onSelected={setTempUnits}
                labelText={'Select A Temperature  Measure Unit:'}
                id={'tempUnits'}
            ></OptionDropDown>
                    <OptionDropDown
                optionsArray={WeatherOptions.precipitationUnits}
                displayParameter='key'
                value='key'
                onSelected={setPrecipitationUnits}
                id={'precipitationUnits'}
                labelText={'Select A Precipitation Measurement  Unit:'}
            ></OptionDropDown>
                <OptionDropDown
                    optionsArray={WeatherOptions.windSpeedUnits}
                    displayParameter='key'
                    value='key'
                    onSelected={setPrecipitationUnits}
                    id={'windSpeedUnits'}
                    labelText={'Select A Precipitation Measurement  Unit:'}
                ></OptionDropDown>

            </div>

        </div>

    );
};

export default LocationWeather