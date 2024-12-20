import React, { useState } from 'react';
import PropTypes from 'prop-types';
import DatePicker from 'react-datepicker';
import Dates from '../CommonUI/Dates'
import OptionDropdown from '../CommonUI/OptionDropdown'
import * as WeatherOptions from "../Weather/WeatherOptions";
import 'react-datepicker/dist/react-datepicker.css';
import { v4 as uuidv4 } from 'uuid';
const MinMaxPopUp = ({  weatherProcessor, addWeatherProcessor,isModalVisible, setIsModalVisible  }) => {
    const [minValue, setMinValue] = useState(weatherProcessor.minValue);
    const [maxValue, setMaxValue] = useState(weatherProcessor.maxValue);
    const [value, setValue] = useState(weatherProcessor.value);
    const [name, setName] = useState(weatherProcessor.name);  // State for the name
    const [startDate, setStartDate] = useState(weatherProcessor.startDate);
    const [endDate, setEndDate] = useState(weatherProcessor.endDate);
    const [dataName, setDataName] = useState(weatherProcessor.dataName);
    const [internalProcessor, setInternalProcessor] = useState(weatherProcessor.internalProcessor);
    const [displayMin, setDisplayMin] = useState(internalProcessor?.hasMin || false);
    const [displayMax, setDisplayMax] = useState(internalProcessor?.hasMax || false);
    const [displayValue, setDisplayValue] = useState(internalProcessor?.hasValue || false);
    const [id,setId]=useState(weatherProcessor.id);


    // Handle min value change
    const handleMinChange = (e) => {
        const value = e.target.value;
        if (!isNaN(value)) {
            setMinValue(value);
        }
    };
    const handleProcessorTypeChange = (item) => {
        setInternalProcessor(item);
        setDisplayMax(internalProcessor.hasMax);
        setDisplayMin(internalProcessor.hasMin);
        setDisplayValue(internalProcessor.hasValue);

    };

    // Handle max value change
    const handleMaxChange = (e) => {
        const value = e.target.value;
        if (!isNaN(value)) {
            setMaxValue(value);
        }
    };
    const handleValueChange = (e) => {
        const value = e.target.value;
        if (!isNaN(value)) {
            setValue(value);
        }
    };
    // Handle name change
    const handleNameChange = (e) => {
        setName(e.target.value);
    };

    // Handle saving data (update weatherProcessor object)
    const handleSave = () => {
        let weatherProcessor= {}
        // Update the weatherProcessor object directly
        weatherProcessor.name = name;
        weatherProcessor.minValue = minValue;
        weatherProcessor.maxValue = maxValue;
        weatherProcessor.startDate = startDate;
        weatherProcessor.endDate = endDate;
        weatherProcessor.dataName=dataName;
        weatherProcessor.internalProcessor=internalProcessor;
        weatherProcessor.id=uuidv4();
        if (name == null || name === '') {
            alert('Name is required and cannot be null or empty.');
            return; // Early exit if validation fails
        }

        if (minValue == null || minValue === '') {
            alert('Min value is required and cannot be null or empty.');
            return;
        }

        if (maxValue == null || maxValue === '') {
            alert('Max value is required and cannot be null or empty.');
            return;
        }

        if (startDate == null || startDate === '') {
            alert('Start date is required and cannot be null or empty.');
            return;
        }

        if (endDate == null || endDate === '') {
            alert('End date is required and cannot be null or empty.');
            return;
        }
        addWeatherProcessor(weatherProcessor);
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
                                <OptionDropdown
                                    optionsArray={WeatherOptions.weatherData}
                                    displayParameter="name"
                                    value="value"
                                    onSelected={setDataName}
                                    labelText="Select A Weather Data Type:"
                                ></OptionDropdown>
                                <OptionDropdown
                                    optionsArray={WeatherOptions.weatherProcessorOptions}
                                    displayParameter='key'
                                    value='value'
                                    id={'key'}
                                    onSelected={handleProcessorTypeChange}
                                    labelText={'Select A Weather Calculation:'}
                                ></OptionDropdown>

                                {/* Name input */}
                                <div className="form-group col-auto d-flex align-items-center text-box-box">
                                    <label htmlFor="startDate" className="date-picker-label flex-shrink-0">Name: </label>
                                    <input
                                        type="text"
                                        className="form-control"
                                        id="name"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        placeholder="Enter a name"
                                    />
                                </div>

                                {/* Min and Max inputs */}
                                {displayMin&&(
                                <div className="form-group col-auto d-flex align-items-center text-box-box">
                                    <label
                                    htmlFor="startDate" className="date-picker-label flex-shrink-0">Min Value:</label>
                                    <input
                                        type="number"
                                        className="form-control"
                                        id="minValue"
                                        value={minValue}
                                        onChange={handleMinChange}
                                        placeholder="Min Value"
                                    />
                                </div>
                                    )}
                                {displayMax&&(
                                    <div className="form-group col-auto d-flex align-items-center text-box-box">
                                    <label htmlFor="startDate" className="date-picker-label flex-shrink-0">Max Value:</label>
                                    <input
                                        type="number"
                                        className="form-control"
                                        id="maxValue"
                                        value={maxValue}
                                        onChange={handleMaxChange}
                                        placeholder="Max Value"
                                    />
                                </div>
                                )}

                                {displayValue&&(
                                    <div className="form-group col-auto d-flex align-items-center text-box-box">
                                        <label htmlFor="startDate" className="date-picker-label flex-shrink-0">Value:</label>
                                        <input
                                            type="number"
                                            className="form-control"
                                            id="maxValue"
                                            value={value}
                                            onChange={handleValueChange}
                                            placeholder="Value"
                                        />
                                    </div>
                                )}

                                {/* Date picker for start and end dates */}
                                <Dates
                                    startText="Start Date:"
                                    endText="End Date:"
                                    setStartDate={(date) => setStartDate(date)}
                                    setEndDate={(date) => setEndDate(date)}
                                    startStartDate={startDate}
                                    startEndDate={endDate}
                                />
                            </div>

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
