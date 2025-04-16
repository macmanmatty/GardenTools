import OpenMap from '../OpenMap/OpenMap.js';
import WeatherProcessors from "./WeatherProcessors";
import WeatherSettings from "./WeatherSettings";
import SettingsPopup from "../Popups/WeatherRequestSettingsPopup"
import FileSettingsPopup from "../Popups/WeatherFileOutputSettingsPopup"
import "../Common.css"

import 'bootstrap/dist/css/bootstrap.min.css';
import Dates from '../CommonUI/Dates'
import React, {useState, useEffect} from "react";
function WeatherRequestEditor() {
    const [weatherRequest, setWeatherRequest] = useState({});
    const [isSettingsVisible, setIsSettingsVisible] = useState(false);
    const [isFileSettingsVisible, setIsFileSettingsVisible] = useState(false);
    const [startDate, setStartDate] = useState(weatherRequest.startDate);
    const [endDate, setEndDate] = useState(weatherRequest.endDate);
    const [weatherProcessors, setWeatherProcessors] = useState(weatherRequest.weatherProcessors ||[]);
    const [weatherLocations, setWeatherLocations] = useState(weatherRequest.weatherLocations||[]);
    const [isWeatherRequestLoaded, setIsWeatherRequestLoaded] = useState(false);  // Flag to track loading

    useEffect(() => {
        // Log the updated weatherRequest after it changes
        console.log(weatherRequest);
        if(weatherRequest !== null && weatherRequest !== undefined && Object.keys(weatherRequest).length > 0) {
           localStorage.setItem("weatherRequest", JSON.stringify(weatherRequest));
        }


    }, [weatherRequest]);
    useEffect(() => {
        let storedWeatherRequest = JSON.parse(localStorage.getItem("weatherRequest"));
        if(!storedWeatherRequest){
            storedWeatherRequest={};
        }
           setWeatherRequest(storedWeatherRequest);
           setIsWeatherRequestLoaded(true);


    },[]);
    // Function to update specific fields in the weatherRequest
    const updateWeatherRequest = (field, value) => {
        console.log("Upated Weather request");
        setWeatherRequest((prevRequest) => ({
            ...prevRequest,
            [field]: value,
        }));
    };


    return (
        <div className="App container  text-cente">
            <h1>Weather App</h1>
            <div className="row mt-3 mb-4">
            </div>

            {/* Settings Modal */}
            <SettingsPopup
                isModalVisible={isSettingsVisible}
                setIsModalVisible={setIsSettingsVisible}
                updateWeatherRequest={updateWeatherRequest}
            />
            <FileSettingsPopup
                isModalVisible={isFileSettingsVisible}
                setIsModalVisible={setIsFileSettingsVisible}
                updateWeatherRequest={updateWeatherRequest}
            />

            {/* Main Content: Bootstrap Grid with two columns */}
            <div className="row">
                {/* Left: Map */}
                <div className="col-md-6 mb-4">
                    <OpenMap weatherRequest={weatherRequest} updateWeatherRequest={updateWeatherRequest}/>
                </div>

                {/* Right: Weather Processors */}
                <div className="col-md-6 mb-4">
                    <div className="col-6 d-flex justify-content-between  gap-2  text-nowrap">
                        <button
                            className="btn btn-primary"
                            onClick={() => setIsSettingsVisible(true)}
                        >
                            Weather Processing Settings
                        </button>
                        <button
                            className="btn btn-primary"
                            onClick={() => setIsFileSettingsVisible(true)}
                        >
                            File Output Settings
                        </button>
                    </div>
                    <Dates
                        startText="Start Date:"
                        endText="End Date:"
                        setStartDate={(date) => setStartDate(date)}
                        setEndDate={(date) => setEndDate(date)}
                        startStartDate={startDate}
                        startEndDate={endDate}
                    />
                    <WeatherProcessors weatherRequest={weatherRequest} updateWeatherRequest={updateWeatherRequest}/>

                </div>
            </div>
        </div>
    );
}


export default WeatherRequestEditor;
