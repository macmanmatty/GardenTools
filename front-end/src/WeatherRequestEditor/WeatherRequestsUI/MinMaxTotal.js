import React, { useState } from 'react';
import 'react-datepicker/dist/react-datepicker.css';
import Dates from "../../CommonUI/Dates";
import OptionDropDown from "../../CommonUI/OptionDropdown";
import * as WeatherOptions from "../WeatherOptions";

const MinMaxTotal = ({weatherRequest}) => {

const [calculationMode, setCalculationMode] = useState("Max");
const setStartDate=(startDate)=>{
    weatherRequest.startDate=startDate;
}
const setEndDate=(endDate)=>{
weatherRequest.endDate=endDate;
}


  return (
    <div>
      <Dates>
          dateFormat={"mm-dd"}
          setEndDate={setEndDate};
          setStartDate={setStartDate};
      </Dates>
        <OptionDropDown
            optionsArray={WeatherOptions.simpleCalculations}
            displayParameter='key'
            onSelected={setCalculationMode}
            labelText={'Select A Calculation Mode:'}
            id={'tempUnits'}
        ></OptionDropDown>


    </div>
  );
};

export default MinMaxTotal