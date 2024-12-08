import React,  { useState} from 'react';
import { Popup} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import EditableLabel from "../CommonUI/EditableLabel";
const LocationPopup = ({marker, onDelete}) => {
    const [checked, setChecked]=useState(marker.selected)
    const setSelected=()=>{
        setChecked(!marker.selected);
        marker.selected=!marker.selected;
    }
  return (
             <Popup>
               Latitude: {marker.position[0]}, Longitude: {marker.position[1]}
                 <p/>
                 <EditableLabel
                     object={marker}
                     property='name'
                     ></EditableLabel>
                 <button onClick={() => onDelete(marker.id)}>
                     Delete
                 </button>
             </Popup>
  )
   };
export default LocationPopup;
