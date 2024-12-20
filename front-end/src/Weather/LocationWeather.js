import React, {useState} from "react";
import Dates from "../CommonUI/Dates";
import OptionDropDown from "../CommonUI/OptionDropdown";
import MinMaxPopUp from  "../Popups/MinMaxPopup";
import * as WeatherOptions from "./WeatherOptions";
import './Weather.css';
import ItemList from "../CommonUI/ItemList";

const LocationWeather = ({weatherRequest}) => {
    // State to manage the selected value
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [tempUnits, setTempUnits]=useState('');
    const [windSpeedUnits, setWindSpeedUnits]=useState('');
    const [precipitationUnits, setPrecipitationUnits]=useState('');
    const [weatherDataTypes, setWeatherDataTypes]=useState([]);
    const [timeZone, setTimeZone]=useState([]);
    const [startChillDate, setStartChillDate]=useState([]);
    const [endChillDate, setEndChillDate]=useState([]);
    const[SelectedCalculationComponent, setSelectedCalculationComponent]=useState('');
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [weatherProcessors, setWeatherProcessors] = useState([]);
    const[currentWeatherProcessor, setCurrentWeatherProcessor]=useState({});



    const addWeatherProcessor = (weatherProcessor) => {
        // Create a copy of the current array and add a new item to it
        const updatedItems = [...weatherProcessors, weatherProcessor];
        setWeatherProcessors(updatedItems);
    };
    const editWeatherProcessor = (weatherProcessor) => {
        // Create a copy of the current array and add a new item to it
        setCurrentWeatherProcessor(weatherProcessor);
        setIsModalVisible(true)
    };
    const createNewWeatherProcessor = () => {
        // Create a copy of the current array and add a new item to it
        setCurrentWeatherProcessor({});
        setIsModalVisible(true)
    };
    const deleteWeatherProcessor = (index) => {
        const updatedItems = weatherProcessors.filter((item, i) => i !== index);
        setWeatherProcessors(updatedItems);
    };
    const addWeatherDataType = (item) => {
        // Create a copy of the current array and add a new item to it
        const updatedItems = [...weatherDataTypes, item];
        // Set the updated array as the new state
        setWeatherDataTypes(updatedItems);
    };






    // Handler function to update the selected value
    const getWeather = (event) => {
        setIsModalVisible(true);
    };
    return (
        <div>
            <div  class='inlineDropdowns'>
                <ItemList
                    handleAddItem={createNewWeatherProcessor}
                    items={weatherProcessors}
                    handleDeleteItem={deleteWeatherProcessor}
                    handleEditItem={editWeatherProcessor}
                    title={"Weather Processors"}
                />
                <MinMaxPopUp
                    weatherProcessor={currentWeatherProcessor}
                    setIsModalVisible={setIsModalVisible}
                    isModalVisible={isModalVisible}
                    addWeatherProcessor={addWeatherProcessor}
                    />
            </div>
            <div>
            </div>

            <div>
            <button
                onClick={getWeather}
            >
                Add Weather

            </button>
            </div>

        </div>

    );
};

export default LocationWeather