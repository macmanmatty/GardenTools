import React,  { useState} from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents, CircleMarker} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
const OpenMap = () => {
  const [markers, setMarkers] = useState([]);
 const MapClickHandler = () => {
     const map = useMapEvents({
       click: (e) => {
         console.log('Clicked at:', e.latlng); // Access latitude and longitude from the event
 const newMarker = {
      id: new Date().getTime(),
      position: [e.latlng.lat, e.latlng.lng],
    };
    setMarkers((prevMarkers) => [...prevMarkers, newMarker]);       },
     });
     return null;
   };
  return (
  <div
  >
    <MapContainer
         center={[51.505, -0.09]}
         zoom={13}
         style={{ width: '100%', height: '500px' }}
        doubleClickZoom= {false}
       >
       <MapClickHandler/>
         <TileLayer
           url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
           attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
         />
         {markers.map((marker) => (
           <CircleMarker key={marker.id} center={marker.position}>
             <Popup>
               Latitude: {marker.position[0]}, Longitude: {marker.position[1]}
             </Popup>
           </CircleMarker>
         ))}
   </MapContainer>
   </div>
     );
   };
export default OpenMap;
