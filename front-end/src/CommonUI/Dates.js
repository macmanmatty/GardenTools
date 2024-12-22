import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';  // Make sure to include Bootstrap
import './CommonUI.css'


const StartAndEndDates = ({ startText, endText, startStartDate, startEndDate, dateFormat, setStartDate, setEndDate }) => {
  const [endDateText, setEndDateText] = useState(startEndDate);
  const [startDateText, setStartDateText] = useState(startStartDate);

  const setPickerStartDate = (date) => {
    const start = new Date(date);
    const end = new Date(endDateText);
    if (start > end && end != null) {
      alert('Start date cannot be greater than End date.');
      return;
    }
    setStartDate(date);
    setStartDateText(date);
  };

  const setPickerEndDate = (date) => {
    const start = new Date(startDateText);
    const end = new Date(date);
    if (start > end && start != null) {
      alert('Start date cannot be greater than End date.');
      return;
    }
    setEndDate(date);
    setEndDateText(date);
  };

  return (
      <div className="form-group col-auto align-items-center text-box-box">
        {/* Start Date Picker */}
        <div className="form-group col-auto">
          <label htmlFor="startDate" className="date-picker-label">{startText}</label>
          <DatePicker
              id="startDate"
              selected={startDateText}
              onChange={(date) => setPickerStartDate(date)}
              dateFormat={dateFormat}
              className="form-control"
              isClearable
              placeholderText={startStartDate}
          />
        </div>

        {/* End Date Picker */}
        <div className="form-group col-auto text-box-box">
          <label htmlFor="endDate" className="date-picker-label">{endText}</label>
          <DatePicker
              id="endDate"
              selected={endDateText}
              onChange={(date) => setPickerEndDate(date)}
              dateFormat={dateFormat}
              className="form-control"
              isClearable
              placeholderText={startEndDate}
          />
        </div>
      </div>
  );
};

export default StartAndEndDates;