import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import './CommonUI.css'

/**
 *
 * @param callOnSelectedOnInitialize if true this will called the onSelected() method on first component render
 * using the initialSelectedOption if no option is passed in the first element in the array of options will be used
 * @param initialSelectedOption the initial selected option
 * @param defaultText the default Displayed text if no option is selected
 * @param optionsArray the array  of selectable options
 * @param displayName the name of the field  to display for each option
 * @param valueName the name of the value field for each option
 * @param onSelected the callback method from  the parent component that is  called when a
 * selected option changes
 * @param labelText the text for the label next to the dropdown
 * @returns {Element} the OptionDropDown  element made of a simple HTML select
 * @constructor
 */
const OptionDropDown = ({callOnSelectedOnInitialize, initialSelectedOption, defaultText, optionsArray, displayName, valueName, onSelected, labelText }) => {
    // State to manage the selected value
    const [selectedOption, setSelectedOption] = useState(initialSelectedOption);
    const [isSelected, setIsSelected] = useState(false);

    /**
     * used to initialize the selected option
     * and if applicable call onSelected()
     */
    // Effect to initialize selectedOption and handle calling onSelected
    useEffect(() => {
        // If the selectedOption is null or undefined, set it to the first item in optionsArray
        if (initialSelectedOption === undefined || initialSelectedOption=== null) {
            if (optionsArray.length > 0) {
                setSelectedOption(optionsArray[0][displayName]);  // Default to the first option
            }
        }
        // If callOnSelectedOnInitialize is true, call the onSelected callback
        if (callOnSelectedOnInitialize && selectedOption !== null) {
            const selectedObject = optionsArray.find(option => option[displayName] === selectedOption);
            if (selectedObject) {
                onSelected(selectedObject[valueName]);
            }
        }
    }, [selectedOption, optionsArray, callOnSelectedOnInitialize, onSelected, displayName, valueName]);

    useEffect(() => {
        if (initialSelectedOption !== selectedOption  && !isSelected) {
            // Update selectedOption only if initialSelectedOption is different
            setSelectedOption(initialSelectedOption);
        }
    }, [initialSelectedOption, selectedOption, isSelected]);


    /**
     * called when the selected option changes
     * @param event
     */
    const handleSelectChange = (event) => {
        setSelectedOption(event.target.value);
        setIsSelected(true);
        const selectedObject = optionsArray.find(option => option[displayName] === event.target.value);
        onSelected(selectedObject[valueName]);
    };

    return (
        <div className="form-group">
            <label htmlFor="dropdownSelect" className="font-weight-bold">{labelText}</label>

            {/* Dropdown/select element */}
            <select
                id="dropdownSelect"
                className="form-control"
                value={selectedOption}
                onChange={handleSelectChange}
            >
                {/* Default/empty option */}
                <option value="">{defaultText}</option>

                {/* Dynamically populated options */}
                {optionsArray.map((option, index) => (
                    <option key={option[displayName]} value={option[displayName]}>
                        {option[displayName]}
                    </option>
                ))}
            </select>
        </div>
    );
};

// Prop types for better code validation
OptionDropDown.propTypes = {
    defaultText: PropTypes.string.isRequired,
    optionsArray: PropTypes.array.isRequired,
    displayName: PropTypes.string.isRequired,
    onSelected: PropTypes.func.isRequired,
    labelText: PropTypes.string.isRequired,
};

export default OptionDropDown;