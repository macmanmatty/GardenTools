import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import 'bootstrap/dist/css/bootstrap.min.css';


const StartAndEndDates = ({ startText, endText, initialStartDate,initialEndDate,
                            dateFormat, setStartDate, setEndDate, showMonthAndDayOnly }) => {
  const [endDateInternal, setEndDateInternal] = useState(initialEndDate ? new Date(initialEndDate) : new Date());
  const [startDateInternal, setStartDateInternal] = useState(initialStartDate ? new Date(initialStartDate) : new Date());
  useEffect(() => {
    console.log("set dates");
    console.log(initialStartDate)
      if (initialStartDate != null) {
        setStartDateInternal(initialStartDate ? new Date(initialStartDate) : new Date());
        setStartDate(initialStartDate ? new Date(initialStartDate) : new Date());
      }
      if (initialEndDate != null) {
        setEndDateInternal(initialEndDate ? new Date(initialEndDate) : new Date());
        setEndDate(initialEndDate ? new Date(initialEndDate) : new Date());
      }

  }, [initialEndDate, initialStartDate]);


  const setPickerStartDate = (date) => {
    const start = new Date(date);
    const end = new Date(endDateInternal);
    const today =new Date();
    if(!date){
      setStartDateInternal(null);
      setStartDate(null);
      return;
    }
    if(start>today){
      alert("We can't predict the future buddy! Please pick a date on or before today");
      if(!end){
        setStartDateInternal(end);
        setStartDate(end);
      }
      else{
      setStartDateInternal(new Date());
      setStartDate(new Date());
      }
      return;

    }
    if (start > end && !end) {
      alert('Start date cannot be greater than End date.');
      return;
    }
    setStartDate(date);
    setStartDateInternal(date);
  };

  const setPickerEndDate = (date) => {
    const start = new Date(startDateInternal);
    const end = new Date(date);
    const today =new Date();
    if(!date){
      setEndDateInternal(null);
      setEndDate(null);

      return;
    }
    if(end>today){
      alert("We can't predict the future buddy! Please pick a date on or before today");
        setEndDateInternal(new Date());
        setEndDate(new Date());
      return;
    }
    if (start > end && start != null) {
      alert('Start date cannot be greater than End date.');
      return;
    }
    setEndDate(date);
    setEndDateInternal(date);
  };

  return (
      <div className="form-group col-auto align-items-center text-box-box">
        {/* Start Date Picker */}
        <div className="form-group col-auto">
          <label htmlFor="startDate" className="date-picker-label">{startText}</label>
          <DatePicker
              id="startDate"
          //   selected={startDateInternal}
              onChange={(date) => setPickerStartDate(date)}
              dateFormat={dateFormat}
              className="form-control"
              isClearable
              placeholderText={initialStartDate}
              {...(showMonthAndDayOnly ? { showDayPicker: true } : {})}

          />
        </div>

        {/* End Date Picker */}
        <div className="form-group col-auto text-box-box">
          <label htmlFor="endDate" className="date-picker-label">{endText}</label>
          <DatePicker
              id="endDate"
         //    selected={endDateInternal}
              onChange={(date) => setPickerEndDate(date)}
              dateFormat={dateFormat}
              className="form-control"
              isClearable
              placeholderText={initialEndDate}
              {...(showMonthAndDayOnly ? { showDayPicker: true } : {})}
          />
        </div>
      </div>
  );
};

export default StartAndEndDates;