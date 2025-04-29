import React, { useState,  useEffect } from 'react';
import PropTypes from 'prop-types';
import Dates from '../CommonUI/Dates'
import OptionDropdown from '../CommonUI/OptionDropdown'
import * as WeatherOptions from "../WeatherRequestEditor/WeatherOptions";
import MonthDayDropdown from '../CommonUI/MonthDayDropdown'
import 'react-datepicker/dist/react-datepicker.css';
import '../Common.css'

/**
 * Pop up to create a new weather processor or edit a weather processor @see WeatherProcessor Class in java
 * @param weatherProcessor the current weather processor may be a  {}
 * @param addWeatherProcessor the callBack Function that is called when add processor is clicked
 * @param isModalVisible  is the popup visible?
 * @param setIsModalVisible visible setter method
 * @returns {Element} The WeatherPopup processor adder
 * @constructor
 */
const WeatherProcessorPopup = ({  weatherProcessor, addWeatherProcessor,isModalVisible, setIsModalVisible  }) => {

    const [changed, setChanged] = useState(false);

    /**
     * min value to process if the weather processor requires a min value
     * */
    const [minValue, setMinValue] = useState(weatherProcessor.minValue);
    /**
     * min value to process if the weather processor requires a min value
     * */
    const [maxValue, setMaxValue] = useState(weatherProcessor.maxValue);
    /**
     * value to process if the weather processor requires a single value
     * */
    const [value, setValue] = useState(weatherProcessor.value);
    /**
     * the processor display name on the front end
     * */
    const [name, setName] = useState(weatherProcessor.name);  // State for the name
    /**
     * the indexes of the state dates and months for the weather processor  index = month / day number -1
     * to match the indexes of the drop down array
     */
    const [startMonth, setStartMonth] = useState(weatherProcessor.startMonth || 1); // Default to current month
    const [endMonth, setEndMonth] = useState(weatherProcessor.endMonth || 1);
    const [startDay, setStartDay] = useState(weatherProcessor.startDay || 12);
    const [endDay, setEndDay] = useState(weatherProcessor.endDay || 31);
    /**
    /**
     * the internal  weather processor data object  is a JavaScript object only used on the front end
     */
    const [internalProcessor, setInternalProcessor] = useState(weatherProcessor.internalProcessor || {});
    /**
     * the OpenMeteo weather data type to process
     */
    const [weatherDataType, setWeatherDataType] = useState(weatherProcessor.hourlyDataType);
    /**
     * the OpenMeteo weather data type to process
     */
    const [weatherDataTypeDisplayName, setWeatherDataTypeDisplayName] = useState(weatherProcessor.hourlyDataType);


    /**
     * the name of the internal weather processor data object
     */
    const [internalProcessorDisplayName, setInternalProcessorDisplayName] = useState(weatherProcessor.internalProcessorDisplayName);
    /**
     * booleans for min, max and value to determine which input boxes to display
     */
    const [displayMin, setDisplayMin] = useState(internalProcessor?.hasMin || false);
    const [displayMax, setDisplayMax] = useState(internalProcessor?.hasMax || false);
    const [displayValue, setDisplayValue] = useState(internalProcessor?.hasValue || false);
    /**
     * booleans to tell the if processor should calculate the average or only calculate the average
     */
    const [calculateAverage, setCalculateAverage] = useState(weatherProcessor?.calculateAverage || true);
    const [onlyCalculateAverage, setOnlyCalculateAverage] = useState(weatherProcessor?.onlyCalulateAverage || false);
    /**
     * internal id of the weather processor  JavaScript Object
     */
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
        setInternalProcessor(internalProcessor );
        setDisplayMax(internalProcessor.hasMax);
        setDisplayMin(internalProcessor.hasMin);
        setDisplayValue(internalProcessor.hasValue);

    };
    const handleWeatherDataTypeChange = (weatherData,  weatherDataDisplayName) => {
        setWeatherDataType(weatherData);
        setWeatherDataTypeDisplayName(weatherDataDisplayName);
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

    };
    const setEndDate = (date) => {
        endDate.current=date;

    };
    const setStartDate = (date) => {
        startDate.current=date;

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

        if ((!internalProcessor || !internalProcessor.processorName)) {
            alert('You Must Select a Weather Processor!');
            return;
        }
        if ((!weatherDataType)) {
            alert('You Must Select a Data Type!');
            return;
        }
        if (endDay==null) {
            alert('End day is required and cannot be null or empty.');
            return;
        }
        if (startDay==null) {
            alert('Start day  is required and cannot be null or empty.');
            return;
        }
        if (endMonth==null) {
            alert('End month is required and cannot be null or empty.');
            return;
        }
        if (startMonth==null) {
            alert('Start month  is required and cannot be null or empty.');
            return;
        }

        let weatherProcessor= {}
        weatherProcessor.name = name;
        weatherProcessor.minValue = minValue;
        weatherProcessor.value=value;
        weatherProcessor.maxValue = maxValue;
        weatherProcessor.startProcessMonth=startMonth+1;
        weatherProcessor.startProcessDay=startDay+1;
        weatherProcessor.endProcessMonth = endMonth+1;
        weatherProcessor.endProcessDay = endDay+1;
        weatherProcessor.hourlyDataType=weatherDataType;
        weatherProcessor.processorName=internalProcessor.processorName;
        weatherProcessor.internalProcessorDisplayName=internalProcessor.displayName;
        weatherProcessor.calculateAverage=calculateAverage;
        weatherProcessor.onlyCalculateAverage=onlyCalculateAverage;
        weatherProcessor.id=id;
        weatherProcessor.weatherDataTypeDisplayName=weatherDataTypeDisplayName;
        if(displayValue){
            weatherProcessor.inputValues=[value]
        }
        if(displayMax && !displayMin){
            weatherProcessor.inputValues=[maxValue]
        }
        if(displayMin && !displayMax){
            weatherProcessor.inputValues=[minValue]
        }
        if(displayMin && displayMax){
            weatherProcessor.inputValues=[minValue, maxValue]
        }
        addWeatherProcessor(weatherProcessor);
        closeModal();
    };

    // Close the modal (you can manage modal visibility through state here)
    const closeModal = () => {
        setIsModalVisible(false);
        setChanged(false);
    }
    return (
        <>
            {/* Modal container visibility controlled by isModalVisible */}
            {isModalVisible&&(
                <div className="modal show" style={{ display: 'block', backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title"> Create New Weather Data Processor</h5>
                                <button type="button" className="close" onClick={closeModal} aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>

                            <div className="modal-body">
                                {/* Modal content */}
                                <OptionDropdown
                                    optionsArray={WeatherOptions.weatherDataTypes}
                                    displayName="key"
                                    valueName="value"
                                    onSelected={handleWeatherDataTypeChange}
                                    labelText="Select A Weather Data Type:"
                                    initialSelectedOption={weatherDataTypeDisplayName ?? WeatherOptions.weatherDataTypes[0]['key']}
                                ></OptionDropdown>
                                <OptionDropdown
                                    optionsArray={WeatherOptions.weatherProcessorOptions}
                                    displayName='key'
                                    valueName='value'
                                    id={'key'}
                                    onSelected={handleProcessorTypeChange}
                                    callOnSelectedOnInitialize={true}
                                    labelText={'Select A Weather Calculation:'}
                                    initialSelectedOption={internalProcessorDisplayName ??  WeatherOptions.weatherProcessorOptions[0]['key']}
                                ></OptionDropdown>
                                <div className="d-flex align-items-center ">
                                    <label className="form-check-label  right-10 bold "
                                           htmlFor="calculateAverage"> Calculate Average
                                    </label>
                                    <input
                                        className="form-check-input"
                                        type="checkbox"
                                        value="calculateAverage"
                                        id="calculateAverage"
                                        checked={calculateAverage || onlyCalculateAverage}
                                        onChange={handleCalculateAverageChange}
                                        callOnSelectedOnInitialize={true}
                                        disabled={onlyCalculateAverage}
                                    />
                                    <label className="form-check-label left-10 right-10 bold"
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
                                <MonthDayDropdown
                                  startMonth={startMonth}
                                  endMonth={endMonth}
                                  startDay={startDay}
                                  endDay={endDay}
                                  setEndDay={setEndDay}
                                  setStartDay={setStartDay}
                                  setEndMonth={setEndMonth}
                                  setStartMonth={setStartMonth}
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
WeatherProcessorPopup.propTypes = {
    weatherProcessor: PropTypes.object.isRequired,

};


export default WeatherProcessorPopup;
