import React,  { useState} from 'react';
import { Popup} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import EditableLabel from "../CommonUI/EditableLabel";
const MarkerPopup = ({marker}) => {
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
                 <span> Compare</span>
                 <input
                     type="checkbox"
                     onChange={setSelected}
                 />
             </Popup>
  )
   };
export default MarkerPopup;
