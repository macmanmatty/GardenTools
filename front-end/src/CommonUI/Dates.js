import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';  // Make sure to include Bootstrap
import './CommonUI.css'


const StartAndEndDates = ({ startText, endText, initialStartDate,initialEndDate, dateFormat, setStartDate, setEndDate }) => {
  const [endDate, setendDate] = useState(initialEndDate);
  const [startDate, setstartDate] = useState(initialStartDate);
  useEffect(() => {
    setstartDate(initialStartDate);
    setendDate(initialEndDate);
  }, [initialStartDate, initialEndDate]);

  const setPickerStartDate = (date) => {
    const start = new Date(date);
    const end = new Date(endDate);
    const today =new Date();
    if(start>today){
      alert("We can't predict the future buddy! Please pick a date on or before today");
      if(!end){
        setstartDate(end);
      }
      else{
      setstartDate(new Date());
      }
      return;

    }
    if (start > end && end != null) {
      alert('Start date cannot be greater than End date.');
      return;
    }
    setStartDate(date);
    setstartDate(date);
  };

  const setPickerEndDate = (date) => {
    const start = new Date(startDate);
    const end = new Date(date);
    const today =new Date();
    if(end>today){
      alert("We can't predict the future buddy! Please pick a date on or before today");
      if(!start) {
        setendDate(new Date());
      }
      else{
        setendDate(start);
      }
      return;

    }
    if (start > end && start != null) {
      alert('Start date cannot be greater than End date.');
      return;
    }
    setEndDate(date);
    setendDate(date);
  };

  return (
      <div className="form-group col-auto align-items-center text-box-box">
        {/* Start Date Picker */}
        <div className="form-group col-auto">
          <label htmlFor="startDate" className="date-picker-label">{startText}</label>
          <DatePicker
              id="startDate"
              selected={startDate}
              onChange={(date) => setPickerStartDate(date)}
              dateFormat={dateFormat}
              className="form-control"
              isClearable
              placeholderText={initialStartDate}
              value={initialStartDate}

          />
        </div>

        {/* End Date Picker */}
        <div className="form-group col-auto text-box-box">
          <label htmlFor="endDate" className="date-picker-label">{endText}</label>
          <DatePicker
              id="endDate"
              selected={endDate}
              onChange={(date) => setPickerEndDate(date)}
              dateFormat={dateFormat}
              className="form-control"
              isClearable
              placeholderText={initialEndDate}
              value={initialEndDate}
          />
        </div>
      </div>
  );
};

export default StartAndEndDates;