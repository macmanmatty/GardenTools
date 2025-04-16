import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import './CommonUI.css'

/**
 *
 * @param callOnSelectedOnInitialize if true this will called the onSelected() method on first component render
 * using the initialSelectedOption if no option is passed in the first element in the array of options will be used
 * @param initialSelectedOption the initial selected option
 * @param selectIndex wether or ot the initial selected option is an index
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
const OptionDropDown = ({callOnSelectedOnInitialize, initialSelectedOption, selectIndex,  defaultText, optionsArray, displayName, valueName, onSelected, labelText, }) => {
    // State to manage the selected value
    const [selectedOption, setSelectedOption] = useState(initialSelectedOption);
    const [selectedIndex, setSelectedIndex] = useState(1);
    const [isSelected, setIsSelected] = useState(false);
    /**
     * used to initialize the selected option
     * and if applicable call onSelected()
     */
    // Effect to initialize selectedOption and handle calling onSelected
    useEffect(() => {
        // Initialize selectedOption when the modal loads or when optionsArray changes
        if (initialSelectedOption === undefined || initialSelectedOption === null) {
            // Set default to the first item in optionsArray if no initialSelectedOption
            if (optionsArray.length > 0) {
                const defaultOption = optionsArray[0][displayName];
                setSelectedOption(defaultOption);
            }
        } else {
            // Set selectedOption based on initialSelectedOption
            const selectedOptionFromIndex = optionsArray[initialSelectedOption];
            if (selectedOptionFromIndex) {
                setSelectedOption(selectedOptionFromIndex[displayName]);
            }
        }
    }, [initialSelectedOption, optionsArray, displayName]);

    useEffect(() => {
        // If callOnSelectedOnInitialize is true, call the onSelected callback
        if (callOnSelectedOnInitialize && selectedOption !== null && !isSelected) {
            const selectedObject = optionsArray.find(option => option[displayName] === selectedOption);
            if (selectedObject) {
                onSelected(selectedObject[valueName], selectedObject[displayName]);
            }
        }
    }, [selectedOption, callOnSelectedOnInitialize, optionsArray, displayName, valueName, isSelected, onSelected]);
    /**
     * called when the selected option changes
     * @param event
     */
    const handleSelectChange = (event) => {
        setSelectedOption(event.target.value);
        setIsSelected(true);
        const index=event.target.selectedIndex;
        let selectedObject;
        if(displayName) {
             selectedObject = optionsArray.find(option => option[displayName] === event.target.value);
        }
        else{
           selectedObject=optionsArray[index];
        }
        console.log("selected Object")
        console.log(selectedObject);
        if(displayName) {
            onSelected(selectedObject[valueName], selectedObject[displayName], index);
        }
        else{
            onSelected(selectedObject, index);
        }
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

                {/* Dynamically populated options */}
                {optionsArray.map((option, index) => (
                    <option key={option[displayName] || index} value={option[displayName] || index}>
                        {option[displayName] || option}
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