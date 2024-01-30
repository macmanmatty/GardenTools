import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

const StartAndEndDates = ({monthOnly, yearOnly,  setStartDate, setEndDate}) => {
  let dateFormat;
  const [endDateText, setEndDateText]= useState('');
  const [startDateText, setStartDateText]= useState('');

  if(monthOnly) {
      dateFormat ="mm-yyyy"
  }
  else{
      dateFormat="mm-dd-yyyy"
  }
  let  setPickerStartDate= (date)=> {
  const start=new Date(date);
  const end=new Date(endDateText);
  if(start < end && end != null){
  alert("start>end");
  return;
  }
  setStartDate(date);
    setStartDateText(date);

  };
  let  setPickerEndDate= (date)=> {
    const start=new Date(startDateText);
    const end=new Date(date);
    if(start > end && start != null){
  alert("start<end");
  return;
    }
  setEndDate(date);
    setEndDateText(date);
    };

  return (
    <div>
      <DatePicker
        selected={endDateText}
        onChange={(date) => setPickerStartDate(date)}
        dateFormat={dateFormat}
        isClearable
        placeholderText="Select a Start date"
      />
          <DatePicker
              selected={startDateText}
              onChange={(date) => setPickerEndDate(date)}
              dateFormat={dateFormat}
              isClearable
              placeholderText="Select a End date"
            />
    </div>
  );
};

export default StartAndEndDates