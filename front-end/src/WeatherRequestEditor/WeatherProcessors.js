import React, {useState} from "react";
import Dates from "../CommonUI/Dates";
import OptionDropDown from "../CommonUI/OptionDropdown";
import WeatherProcessorPopup from  "../Popups/WeatherProcessorPopup";
import * as WeatherOptions from "./WeatherOptions";
import ItemList from "../CommonUI/ItemList";

const LocationWeather = ({weatherRequest, updateWeatherRequest}) => {
    // State to manage the selected value
    const[SelectedCalculationComponent, setSelectedCalculationComponent]=useState('');
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [weatherProcessors, setWeatherProcessors] = useState(weatherRequest?.weatherProcessors||[]);
    const[currentWeatherProcessor, setCurrentWeatherProcessor]=useState({});

    const addWeatherProcessor = (weatherProcessor) => {
        // Create a copy of the current array and add a new item to it
        const updatedItems = [...weatherProcessors, weatherProcessor];
        setWeatherProcessors(updatedItems);
        updateWeatherRequest("weatherProcessors",updatedItems)
    };

    const editWeatherProcessor = (weatherProcessor) => {
        console.log('Editing item:', weatherProcessor);  // Log the item to verify it's correct
        setCurrentWeatherProcessor(weatherProcessor);
        setIsModalVisible(true)
    };
    const createNewWeatherProcessor = () => {
        setCurrentWeatherProcessor({});
        setIsModalVisible(true)
    };
    const deleteWeatherProcessor = (index) => {
        const updatedItems = weatherProcessors.filter((item, i) => i !== index);
        setWeatherProcessors(updatedItems);
        updateWeatherRequest("weatherProcessors",updatedItems)
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
                <WeatherProcessorPopup
                    weatherProcessor={currentWeatherProcessor}
                    setIsModalVisible={setIsModalVisible}
                    isModalVisible={isModalVisible}
                    addWeatherProcessor={addWeatherProcessor}
                    />
            </div>
            <div>
            </div>
            </div>

    );
};

export default LocationWeather