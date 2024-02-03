import React, {useState} from 'react';
import 'react-datepicker/dist/react-datepicker.css';
import Dates from "../../CommonUI/Dates";
import MinAndMax from "../../CommonUI/MinAndMax";

const MinAndMaxChill = ({weatherRequest}) => {
  const [maxTemp, setMaxTemp]=useState("45");
  const [minTemp, setMinTemp]=useState("32");
  const hourlyWeatherProcessRequests=weatherRequest.hourlyWeatherProcessRequests;
  const hourlyWeatherProcessRequest={
      values:[],
      startProcessDay: 1,
      startProcessMoth:11,
      endProcessDay: 31,
      endProcessMoth:3,
      hourlyDateType: "temperature_2m",
      processorName: "minMaxChill"
  }


  const setStartDate=(startDate)=>{
    weatherRequest.startDate=startDate;
  }
  const setEndDate=(endDate)=> {
      weatherRequest.endDate = endDate;

  }
 const addHourlyWeatherProcessRequest= ()=>{
      hourlyWeatherProcessRequests.push(hourlyWeatherProcessRequest);

 };
  return (
    <div>
        <h1> Calculate Yearly Chill</h1>
      <Dates>
        dateFormat={"mm-dd"}
        setEndDate={setEndDate}
        setStartDate={setStartDate}
      </Dates>
      <MinAndMax>
        setMin={setMinTemp}
        setMax={setMaxTemp}
      </MinAndMax>
    </div>
  );
};

export default MinAndMaxChill