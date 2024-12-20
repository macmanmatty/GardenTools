import React, { useState } from 'react';
import PropTypes from 'prop-types';
import './commonUI.css'

const OptionDropDown = ({ defaultText, optionsArray, displayParameter, valueName, onSelected, labelText }) => {
    // State to manage the selected value
    const [selectedOption, setSelectedOption] = useState('');

    // Handler function to update the selected value
    const handleSelectChange = (event) => {
        setSelectedOption(event.target.value);
        console.log( "event "+ event.target.value)
        const selectedObject = optionsArray.find(option => option[displayParameter] === event.target.value);
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
                    <option key={option[displayParameter]} value={option[displayParameter]}>
                        {option[displayParameter]}
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
    displayParameter: PropTypes.string.isRequired,
    onSelected: PropTypes.func.isRequired,
    labelText: PropTypes.string.isRequired,
};

export default OptionDropDown;