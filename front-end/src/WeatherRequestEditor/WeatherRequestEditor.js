import OpenMap from '../OpenMap/OpenMap.js';
import WeatherProcessors from "./WeatherProcessors";
import WeatherSettings from "./WeatherSettings";
import SettingsPopup from "../Popups/WeatherRequestSettingsPopup"
import 'bootstrap/dist/css/bootstrap.min.css';
import Dates from '../CommonUI/Dates'
import React, {useState, useEffect} from "react";
function WeatherRequestEditor() {
    const [weatherRequest, setWeatherRequest] = useState({});
    const [isSettingsVisible, setIsSettingsVisible] = useState(false);
    const [startDate, setStartDate] = useState(weatherRequest.startDate);
    const [endDate, setEndDate] = useState(weatherRequest.endDate);
    useEffect(() => {
        // Log the updated weatherRequest after it changes
        console.log(weatherRequest);
    }, [weatherRequest]);

    // Function to update specific fields in the weatherRequest
    const updateWeatherRequest = (field, value) => {
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


            {/* Main Content: Bootstrap Grid with two columns */}
            <div className="row">
                {/* Left: Map */}
                <div className="col-md-6 mb-4">
                    <OpenMap weatherRequest={weatherRequest} updateWeatherRequest={updateWeatherRequest}/>
                </div>

                {/* Right: Weather Processors */}
                <div className="col-md-6 mb-4">
                    <div className="col-6 text-end">
                        <button
                            className="btn btn-primary"
                            onClick={() => setIsSettingsVisible(true)}
                        >
                           Weather Processing Settings
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
