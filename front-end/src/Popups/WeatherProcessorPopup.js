import React, { useState } from 'react';
import PropTypes from 'prop-types';
import DatePicker from 'react-datepicker';
import Dates from '../CommonUI/Dates'
import OptionDropdown from '../CommonUI/OptionDropdown'
import * as WeatherOptions from "../WeatherRequestEditor/WeatherOptions";
import 'react-datepicker/dist/react-datepicker.css';
import '../common.css'
import { v4 as uuidv4 } from 'uuid';
const WeatherProcessorPopup = ({  weatherProcessor, addWeatherProcessor,isModalVisible, setIsModalVisible  }) => {
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
    const [calculateAverage, setCalculateAverage] = useState(weatherProcessor?.calculateAverage || true);
    const [onlyCalculateAverage, setOnlyCalculateAverage] = useState(weatherProcessor?.onlyCalulateAverage || false);

    const [id,setId]=useState(weatherProcessor.id);


    // Handle min value change
    const handleMinChange = (e) => {
        const value = e.target.value;
        if (!isNaN(value)) {
            setMinValue(value);
        }
    };
    const handleCalculateAverageChange = (event) => {
        setCalculateAverage(event.target.checked);
    };

    // Step 3: Handle checkbox change for onlyCalculateAverage
    const handleOnlyCalculateAverageChange = (event) => {
        setOnlyCalculateAverage(event.target.checked);
    };

    const handleProcessorTypeChange = (internalProcessor ) => {
        console.log(internalProcessor)
        setInternalProcessor(internalProcessor );
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
    const handleOnlyCalculateAverage = (e) => {
        const checked=e.target.value;
        setOnlyCalculateAverage(checked);
        if(checked=true){

        }
    };

    // Handle saving data (update weatherProcessor object)
    const handleSave = () => {
        if ((name == null || name === '')) {
            alert('Name is required and cannot be null or empty.');
            return; // Early exit if validation fails
        }

        if ((minValue == null || minValue === '') && displayMin) {
            alert('Min value is required  and must be a Real Number.');
            return;
        }

        if ((maxValue == null || maxValue === '') && displayMax) {
            alert('Max value is required and  must be a Real  Number.');
            return;
        }
        if ((value == null || value === '') && displayValue) {
            alert('Value is required and  must be a Real  Number.');
            return;
        }

        if ((startDate == null || startDate === '') && displayValue) {
            alert('Start date is required and cannot be null or empty.');
            return;
        }
        if ((!internalProcessor || !internalProcessor.processorName)) {
            alert('You Must Select a Weather Processor!');
            return;
        }
        if ((!dataName)) {
            alert('You Must Select a Data Type!');
            return;
        }
        if (!endDate) {
            alert('End date is required and cannot be null or empty.');
            return;
        }
        if (!startDate) {
            alert('Start date is required and cannot be null or empty.');
            return;
        }
        let weatherProcessor= {}
        // Update the weatherProcessor object directly
        weatherProcessor.name = name;
        weatherProcessor.minValue = minValue;
        weatherProcessor.value=value;
        weatherProcessor.maxValue = maxValue;
        weatherProcessor.endDate=endDate;
        weatherProcessor.startDate=startDate;
        weatherProcessor.startProcessMonth=startDate.getMonth() + 1;
        weatherProcessor.startProcessDay=startDate.getDate();
        weatherProcessor.endProcessMonth = endDate.getMonth()+1;
        weatherProcessor.endProcessDay = endDate.getDate();
        weatherProcessor.hourlyDataType=dataName;
        weatherProcessor.processorName=internalProcessor.processorName;
        weatherProcessor.calculateAverage=calculateAverage;
        weatherProcessor.onlyCalculateAverage=onlyCalculateAverage;
        weatherProcessor.id=uuidv4();
        if(displayValue){
            weatherProcessor.inputValues=[value]
        }
        if(displayMax && !DisplayMin){
            weatherProcessor.inputValues=[max]
        }
        if(displayMin && !DisplayMax){
            weatherProcessor.inputValues=[min]
        }
        if(displayMin && DisplayMax){
            weatherProcessor.inputValues=[min, max]
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
                                    displayParameter="key"
                                    valueName="value"
                                    onSelected={setDataName}
                                    labelText="Select A Weather Data Type:"
                                ></OptionDropdown>
                                <OptionDropdown
                                    optionsArray={WeatherOptions.weatherOptions}
                                    displayParameter='key'
                                    valueName='value'
                                    id={'key'}
                                    onSelected={handleProcessorTypeChange}
                                    labelText={'Select A Weather Calculation:'}
                                ></OptionDropdown>
                                <div className="d-flex align-items-center ">
                                    <label className="form-check-label  right-10"
                                           htmlFor="calculateAverage"> Calculate Average
                                    </label>
                                    <input
                                        className="form-check-input"
                                        type="checkbox"
                                        value="calculateAverage"
                                        id="calculateAverage"
                                        checked={calculateAverage || onlyCalculateAverage}
                                        onChange={handleCalculateAverageChange}
                                        disabled={onlyCalculateAverage}
                                    />
                                    <label className="form-check-label left-10 right-10"
                                           htmlFor="onlyCalculateAverage">Only Calculate Average
                                    </label>
                                    <input
                                        className="form-check-input"
                                        type="checkbox"
                                        value="onlyCalculateAverage"
                                        id="onlyCalculateAverage"
                                        checked={onlyCalculateAverage}
                                        onChange={handleOnlyCalculateAverageChange}
                                    />

                                </div>

                                {/* Name input */}
                                <div className="form-group col-auto d-flex align-items-center text-box-box">
                                    <label htmlFor="startDate"
                                           className="date-picker-label flex-shrink-0">Name: </label>
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
                                {displayMin && (
                                    <div className="form-group col-auto d-flex align-items-center text-box-box">
                                        <label
                                            htmlFor="startDate" className="date-picker-label flex-shrink-0">Min
                                            Value:</label>
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
                                {displayMax && (
                                    <div className="form-group col-auto d-flex align-items-center text-box-box">
                                        <label htmlFor="startDate" className="date-picker-label flex-shrink-0">Max
                                            Value:</label>
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

                                {displayValue && (
                                    <div className="form-group col-auto d-flex align-items-center text-box-box">
                                        <label htmlFor="startDate"
                                               className="date-picker-label flex-shrink-0">Value:</label>
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


export default WeatherProcessorPopup;
