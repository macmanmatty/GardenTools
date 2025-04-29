import React, { useState, useEffect } from 'react';
import OptionDropdown from '../CommonUI/OptionDropdown'
function MonthDayDropdown({ startMonth,
endMonth, startDay, endDay, setStartMonth, setEndMonth, setStartDay, setEndDay}) {
  const [daysInStartMonth, setDaysInStartMonth] = useState([]);
  const [daysInEndMonth, setDaysInEndMonth] = useState([]);

  // List of month names
  const months = [
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
  ];

  // Function to calculate days in a given month (Feb always 28)
  const calculateDaysInMonth = (month) => {
    let daysInMonth;
    if (month === 2) { // February (index 1)
      daysInMonth = 28; // February has 28 days (ignoring leap years)
    } else if ([4, 6, 9, 11].includes(month)) { // April, June, September, November (30 days)
      daysInMonth = 30;
    } else { // Other months have 31 days
      daysInMonth = 31;
    }
    return Array.from({ length: daysInMonth }, (_, i) => i+1 );
  };

  useEffect(() => {
    setDaysInStartMonth(calculateDaysInMonth(startMonth));
    setDaysInEndMonth(calculateDaysInMonth(endMonth));
  }, [startMonth, endMonth]);

  const handleStartMonthChange =(month, index) => {
    setStartMonth(index+1);
  };

  const handleEndMonthChange = (month, index) => {
    setEndMonth(index+1);
  };

  const handleStartDayChange = (day, index) => {
    setStartDay(index);
  };

  const handleEndDayChange = (day, index) => {
    setEndDay(index);
  };

  return (
      <div className="container mt-4">
        <div className="row">
          {/* Start Date Section */}
          <div className="col-md-5">
            <OptionDropdown
                optionsArray={months}
                id="startMonth"
                onSelected={handleStartMonthChange}
                labelText="Start Month:"
                initialSelectedOption={startMonth}
                selectIndex={true}
            />
            <OptionDropdown
                optionsArray={daysInStartMonth}
                id="startDay"
                onSelected={handleStartDayChange}
                labelText="Start Day:"
                initialSelectedOption={startDay}
                selectIndex={true}

            />
          </div>

          {/* End Date Section */}
          <div className="col-md-5">
            <OptionDropdown
                optionsArray={months}
                id="endMonth"
                onSelected={handleEndMonthChange}
                labelText="End Month:"
                initialSelectedOption={endMonth}
                selectIndex={true}

            />
            <OptionDropdown
                optionsArray={daysInEndMonth}
                id="endDay"
                onSelected={handleEndDayChange}
                labelText="End Day:"
                initialSelectedOption={endDay}
                selectIndex={true}

            />
          </div>
        </div>
      </div>
  );
}

export default MonthDayDropdown;
