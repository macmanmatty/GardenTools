import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const MinAndMax = ({ setMin, setMax}) => {
  const [minText, setMinText] = useState('');
  const [maxText, setMaxText] = useState('');

  let  setMinTextValue= (value)=> {
    if (/^-?\d*\.?\d*$/.test(value)) {
      setMin(parseFloat(value));
      setMinText(value)
    }
  };
  let  setMaxTextValue= (value)=> {
    if (/^-?\d*\.?\d*$/.test(value)) {
      setMax(parseFloat(value));
      setMaxText(value)
    }

  };

  return (
    <div>
      <input
          type="text"
          value={value}
          onChange={setMinTextValue}
          placeholder="Enter Min Value"
      />
      <input
          type="text"
          value={value}
        onChange={setMaxTextValue}
          placeholder="Enter Max Value"
      />
    </div>
  );
};

export default MinAndMax