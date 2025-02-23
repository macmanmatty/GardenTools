import React, { useState,useEffect } from 'react';
import PropTypes from 'prop-types';
import OptionDropDown from '../CommonUI/OptionDropdown'
import * as WeatherOptions from "../WeatherRequestEditor/WeatherOptions";
import 'react-datepicker/dist/react-datepicker.css';
const SettingsPopup = ({weatherRequest, updateWeatherRequest, isModalVisible, setIsModalVisible  }) => {
    const [tempUnits, setTempUnits]=useState(weatherRequest?.temperatureUnit ||'');
    const [windSpeedUnits, setWindSpeedUnits]=useState(weatherRequest?.windSpeedUnit ||'');
    const [precipitationUnits, setPrecipitationUnits]=useState(weatherRequest?.precipitationUnit||'');
    const [populateLocationData, setPopulateLocationData] = useState(weatherRequest?.populateLocationData || true);
    const [windSpeedUnitsDisplayName, setWindSpeedUnitsDisplayName]=useState(weatherRequest?.windSpeedUnit ||'');
    const [precipitationUnitsDisplayName, setPrecipitationUnitsDisplayName]=useState(weatherRequest?.precipitationUnit||'');
    const [tempUnitsDisplayName, setTempUnitsDisplayName]=useState(weatherRequest?.temperatureUnit ||'');

    // Handle saving DataDisplayName (update weatherProcessor object)
    const handleSave = () => {
        console.log("temp unit :", tempUnits)
        updateWeatherRequest("temperatureUnit",  tempUnits);
        updateWeatherRequest("windSpeedUnit", windSpeedUnits);
        updateWeatherRequest("precipitationUnit", precipitationUnits);
        closeModal();
    };
    useEffect(() => {
        // Check if weatherRequest exists and update state accordingly
        if (weatherRequest) {
            setTempUnits(weatherRequest.temperatureUnit || '');
            setWindSpeedUnits(weatherRequest.windSpeedUnit || '');
            setPrecipitationUnits(weatherRequest.precipitationUnit || '');
            setPopulateLocationData(weatherRequest.populateLocationData ?? true); // `??` ensures default when null or undefined
        }
    }, [weatherRequest]);

    const handlePopulateLocationDataChange = (event) => {
        setPopulateLocationData(event.target.checked);
    };
    const handleTempUnitsChange = (value, name) => {
        setTempUnits(value);
        setTempUnitsDisplayName(name);

    };
    const handlePrecipitationUnitsChange = (value, name) => {
        setPrecipitationUnits(value);
        setPrecipitationUnitsDisplayName(name);

    };
    const handleWindSpeedUnitsChange = (value, name) => {
        setWindSpeedUnits(value);
        setWindSpeedUnitsDisplayName(name);

    };

    // Close the modal (you can manage modal visibility through state here)
    const closeModal = () => {
        setIsModalVisible(false);  // Hide the modal by setting the state to false    };
    }
    
    return (
        <>
            {/* Modal container visibility controlled by isModalVisible */}
            {isModalVisible&&(
                <div className="modal show" style={{ display: 'block', backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title"> Min / Max Weather Data Processor</h5>
                                <button type="button" className="btn-close" onClick={closeModal} aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                <div className="d-flex align-items-center ">
                                    <label className="form-check-label  right-10"
                                           htmlFor="populateLocationData"> Populate Location Data
                                    </label>
                                    <input
                                        className="form-check-input"
                                        type="checkbox"
                                        value="populateLocationData"
                                        id="populateLocationData"
                                        checked={populateLocationData}
                                        onChange={handlePopulateLocationDataChange}
                                    />
                                </div>
                                {/* Modal content */}
                                <OptionDropDown
                                    initialSelectedOption={tempUnits}
                                    optionsArray={WeatherOptions.temperatureUnits}
                                    displayName='key'
                                    valueName='key'
                                    onSelected={handleTempUnitsChange}
                                    labelText={'Select A Temperature  Measure Unit:'}
                                    id={'tempUnits'}
                                ></OptionDropDown>

                                <OptionDropDown
                                    initialSelectedOption={precipitationUnits}
                                    optionsArray={WeatherOptions.precipitationUnits}
                                    displayName='key'
                                    valueName='key'
                                    onSelected={handlePrecipitationUnitsChange}
                                    id={'precipitationUnits'}
                                    labelText={'Select A Precipitation Measurement  Unit:'}
                                ></OptionDropDown>
                                <OptionDropDown
                                    initialSelectedOption={windSpeedUnits}
                                    optionsArray={WeatherOptions.windSpeedUnits}
                                    displayName='key'
                                    valueName='key'
                                    onSelected={handleWindSpeedUnitsChange}
                                    id={'windSpeedUnits'}
                                    labelText={'Select A Precipitation Measurement  Unit:'}
                                ></OptionDropDown>
                            </div>
                                {/* Name input */}
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={closeModal}>
                                    Close
                                </button>
                                <button type="button" className="btn btn-primary" onClick={handleSave}>
                                    Save
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
};


export default SettingsPopup;
