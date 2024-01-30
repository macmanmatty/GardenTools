import  React, { useState } from 'react';
import optionsArray from "leaflet";

const OptionDropDown = ({ optionsArray, displayParameter,  onSelected, labelText}) => {
  // State to manage the selected value
  const [selectedOption, setSelectedOption] = useState('')

    // Handler function to update the selected value
  const handleSelectChange = (event) => {
      onSelected(event.target.selected);
  };
  return (
    <div>
      <label
          className={'labelText'}
      >{labelText} </label>
      {/* Dropdown/select element */}
      <select value={selectedOption} onChange={handleSelectChange}>
        {/* Default/empty option */}
        <option value="">Select Weather Data...</option>
               {optionsArray.map((option, index) => (
                 <option key={option[displayParameter]} value={option[displayParameter]}>
                   {option[displayParameter]}
                 </option>)) }
                   </select>
    </div>
  );
};

export default OptionDropDown;