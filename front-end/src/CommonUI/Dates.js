import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import '../Weather/Weather.css'

const StartAndEndDates = ({ startText, endText, dateFormat,  setStartDate, setEndDate}) => {
  const [endDateText, setEndDateText]= useState('');
  const [startDateText, setStartDateText]= useState('');

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
    <div className='inlineDropdowns'>
      {startText} <DatePicker
        selected={endDateText}
        onChange={(date) => setPickerStartDate(date)}
        dateFormat={dateFormat}
        isClearable
        placeholderText={endText}
      />
      {endText}  <DatePicker
              selected={startDateText}
              onChange={(date) => setPickerEndDate(date)}
              dateFormat={dateFormat}
              isClearable
              placeholderText={startText}
            />
    </div>
  );
};

export default StartAndEndDates