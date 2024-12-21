import OpenMap from '../OpenMap/OpenMap.js';
import WeatherProcessors from "./WeatherProcessors";
import WeatherSettings from "./WeatherSettings";
import SettingsPopup from "../Popups/WeatherRequestSettingsPopup"
import 'bootstrap/dist/css/bootstrap.min.css';
import React, {useState} from "react";
function WeatherRequestEditor() {
    const [weatherRequest, setWeatherRequest] = useState({});
    const [isSettingsVisible, setIsSettingsVisible] = useState(false);

    // Function to update specific fields in the weatherRequest
    const updateWeatherRequest = (field, value) => {
        setWeatherRequest((prevRequest) => ({
            ...prevRequest,
            [field]: value,
        }));
    };

    return (
        <div className="App container">
            {/* Header */}
            <div className="row mt-3 mb-4">
                <div className="col-6">
                    <h1>Weather App</h1>
                </div>
                <div className="col-6 text-end">
                    <button
                        className="btn btn-primary"
                        onClick={() => setIsSettingsVisible(true)}
                    >
                        Settings
                    </button>
                </div>
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
                    <OpenMap weatherRequest={weatherRequest} updateWeatherRequest={updateWeatherRequest} />
                </div>

                {/* Right: Weather Processors */}
                <div className="col-md-6 mb-4">
                    <WeatherProcessors weatherRequest={weatherRequest} updateWeatherRequest={updateWeatherRequest} />
                </div>
            </div>
        </div>
    );
}


export default WeatherRequestEditor;
