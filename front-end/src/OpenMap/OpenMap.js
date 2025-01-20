import React,  { useState} from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents, CircleMarker} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import MarkerPopupLabel from "./LocationPopup";
const OpenMap = ({weatherRequest, updateWeatherRequest}) => {
  const [markers, setMarkers] = useState([]);
    const [locations, setLocations] = useState(weatherRequest?.locations||[]);

    const maxLocations=6;
    const onDelete=(id)=> {
      console.log("id "+id);
        setMarkers((prevMarkers) => prevMarkers.filter((marker) => marker.id !== id));
    };
 const MapClickHandler = () => {
     const map = useMapEvents({
       dblclick: (e) => {
           const newMarker = {
     id: new Date().getTime(),
      position: [e.latlng.lat, e.latlng.lng],
     latitude: e.latlng.lat,
     longitude: e.latlng.lng,
     name:"Marker "+markers.length,
     selected: false
    };
           //weatherRequest.locations.push(newMarker);
           setMarkers((prevMarkers) => [...prevMarkers, newMarker]);
           },
     });
     return null;
   };

  return (
  <div
  >
    <MapContainer
         center={[38.60713981232172,  -90.1337242126464844]}
         zoom={5}
         style={{ width: '100%', height: '500px' }}
        doubleClickZoom= {false}
       >
       <MapClickHandler/>
         <TileLayer
           url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
           attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
         />
         {markers.map((marker) => (
           <CircleMarker
               key={marker.id}
               center={marker.position}>
               <MarkerPopupLabel
                   marker={marker}
                   onDelete={onDelete}
               />

           </CircleMarker>

         ))}
   </MapContainer>
   </div>
     );
   };
export default OpenMap;
