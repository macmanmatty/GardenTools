import React, {useState, useEffect} from "react";
import Dates from "../CommonUI/Dates";
import OptionDropDown from "../CommonUI/OptionDropdown";
import WeatherProcessorPopup from  "../Popups/WeatherProcessorPopup";
import * as WeatherOptions from "./WeatherOptions";
import ItemList from "../CommonUI/ItemList";
import { v4 as uuidv4 } from 'uuid';


const WeatherProcessors = ({weatherRequest, updateWeatherRequest}) => {
    // State to manage the selected value
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [weatherProcessors, setWeatherProcessors] = useState(weatherRequest?.weatherProcessors||[]);
    const[currentWeatherProcessor, setCurrentWeatherProcessor]=useState({});
    useEffect(() => {
        if( weatherRequest.weatherProcessors ) {
            setWeatherProcessors(weatherRequest.weatherProcessors);
        }

    }, [weatherRequest]);

    const createNewWeatherProcessor = () => {
        let weatherProcessor={};
        weatherProcessor.id=uuidv4();
        setCurrentWeatherProcessor(weatherProcessor);
        setIsModalVisible(true);
    };
    const addWeatherProcessor = (weatherProcessor) => {
        // Check if the weatherProcessor already exists based on id
        const foundItem = weatherProcessors.find(processor => processor.id === weatherProcessor.id);
        let updatedItems;
        if (!foundItem) {
            // If the processor doesn't exist, add it to the array
            updatedItems = [...weatherProcessors, weatherProcessor];
        } else {
            // If it exists, update the existing processor
            updatedItems = weatherProcessors.map(processor =>
                processor.id === weatherProcessor.id ? { ...processor, ...weatherProcessor } : processor
            );
        }

        // Update the state with the updated list of processors
        setWeatherProcessors(updatedItems);

        // Update the weather request with the new array of processors
        updateWeatherRequest("weatherProcessors", updatedItems);
    };
    const editWeatherProcessor = (weatherProcessor) => {
        console.log("edit this");
        console.log(weatherProcessor);
       setCurrentWeatherProcessor(weatherProcessor);
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
                    key={currentWeatherProcessor.id}
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

export default WeatherProcessors