import React, { useState } from 'react';
import PropTypes from 'prop-types';
import OptionDropDown from '../CommonUI/OptionDropdown'
import * as WeatherOptions from "../WeatherRequestEditor/WeatherOptions";
import 'react-datepicker/dist/react-datepicker.css';
const MinMaxPopUp = ({ updateWeatherRequest, isModalVisible, setIsModalVisible  }) => {
    const [tempUnits, setTempUnits]=useState('');
    const [windSpeedUnits, setWindSpeedUnits]=useState('');
    const [precipitationUnits, setPrecipitationUnits]=useState('');

    // Handle saving data (update weatherProcessor object)
    const handleSave = () => {
        updateWeatherRequest("temperatureUnit", tempUnits);
        updateWeatherRequest("windSpeedUnit", windSpeedUnits);
        updateWeatherRequest("precipitationUnit", precipitationUnits);
        closeModal();
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
                                <button type="button" className="close" onClick={closeModal} aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>

                            <div className="modal-body">
                                {/* Modal content */}
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


export default MinMaxPopUp;
