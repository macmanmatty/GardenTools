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

        // If the selectedOption is null or undefined, set it to the first item in optionsArray
        if (initialSelectedOption === undefined || initialSelectedOption=== null) {
            if (optionsArray.length >0  && displayName && !selectIndex) {
                setSelectedOption(optionsArray[0][displayName]);  // Default to the first option
            }
            else{
                setSelectedOption(optionsArray[0]);  // Default to the first option
            }

        }
        if(selectIndex && !isSelected ){
            console.log("selected index");
            console.log(initialSelectedOption);
            console.log(optionsArray[initialSelectedOption]);
            setSelectedOption(optionsArray[initialSelectedOption]);
        }
        else if(!isSelected){
            setSelectedOption(initialSelectedOption);
        }

        // If callOnSelectedOnInitialize is true, call the onSelected callback
        if (callOnSelectedOnInitialize && selectedOption !== null) {
            const selectedObject = optionsArray.find(option => option[displayName] === selectedOption);
            if (selectedObject && displayName && valueName) {
                onSelected(selectedObject[valueName], selectedObject[displayName]);
            }
            else if(selectedObject){
                onSelected(selectedObject, selectedObject);
            }
        }
    }, [selectedOption, optionsArray, callOnSelectedOnInitialize, onSelected, displayName, valueName]);


    useEffect(() => {
        // When component unmounts, reset `isSelected` to false
        return () => {
            setIsSelected(false);
        };
    }, []);  // Empty dependency array ensures this effect only runs on unmount

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