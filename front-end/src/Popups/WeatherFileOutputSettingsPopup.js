import React, { useState,useEffect } from 'react';
import PropTypes from 'prop-types';
import OptionDropDown from '../CommonUI/OptionDropdown'
import * as WeatherOptions from "../WeatherRequestEditor/WeatherOptions";
import 'react-datepicker/dist/react-datepicker.css';
const SettingsPopup = ({weatherRequest, updateWeatherRequest, isModalVisible, setIsModalVisible  }) => {
    const [outputFileType, setOutputFileType]=useState(weatherRequest?.outputFileType||'JSON');
    const [populateLocationData, setPopulateLocationData] = useState(weatherRequest?.populateLocationData || true);

    // Handle saving DataDisplayName (update weatherProcessor object)
    const handleSave = () => {
        updateWeatherRequest("outputFileType",  outputFileType);
        closeModal();
    };
    useEffect(() => {
        // Check if weatherRequest exists and update state accordingly
        if (weatherRequest) {
            setOutputFileType(weatherRequest.outputFileType || 'JSON');
        }
    }, [weatherRequest]);

    const handlePopulateLocationDataChange = (event) => {
        setPopulateLocationData(event.target.checked);
    };
    const handleOutputFileTypeChange = (value, name) => {
        setOutputFileType(value);
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
                                    initialSelectedOption={outputFileType}
                                    optionsArray={WeatherOptions.fileTypes}
                                    displayName='key'
                                    valueName='key'
                                    onSelected={handleOutputFileTypeChange}
                                    labelText={'Select A File Type'}
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
