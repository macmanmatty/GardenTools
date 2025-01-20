import React, {useState} from 'react';
import 'react-datepicker/dist/react-datepicker.css';
import Dates from "../../CommonUI/Dates";

const MinAndMaxChill = ({weatherRequest}) => {
  const [maxTemp, setMaxTemp]=useState("45");
  const [minTemp, setMinTemp]=useState("32");
  const  [startDate, setStartDate]=useState(new Date());
    const  [endDate, setEndDate]=useState(new Date());

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



    const addHourlyWeatherProcessRequest= ()=>{
      hourlyWeatherProcessRequest.values[0]=minTemp;
    hourlyWeatherProcessRequest.values[1]=maxTemp;
    hourlyWeatherProcessRequest.startProcessDay= startDate.getDay();
    hourlyWeatherProcessRequest.startProcessMoth=startDate.getMonth();
    hourlyWeatherProcessRequest.endProcessDay=endDate.getDay();
    hourlyWeatherProcessRequest.endProcessMonth=endDate.getMonth();
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

        <button>
            onClick={addHourlyWeatherProcessRequest}
        </button>
    </div>
  );

};

export default MinAndMaxChill