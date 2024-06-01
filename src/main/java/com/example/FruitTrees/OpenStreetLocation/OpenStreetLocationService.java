package com.example.FruitTrees.OpenStreetLocation;

import com.example.FruitTrees.Location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

@Service
public class OpenStreetLocationService {


    private final OpenStreetLocationHTTPRequest openStreetLocationHTTPRequest;
   @Autowired
    public OpenStreetLocationService(OpenStreetLocationHTTPRequest openStreetLocationHTTPRequest) {
       this.openStreetLocationHTTPRequest = openStreetLocationHTTPRequest;
    }



        public Location populateLocationData(Location location ) throws IOException {
       OpenStreetLocationResponse openStreetLocationResponse = getLocationData(location);
       populateLocationData(location, openStreetLocationResponse);
        return  location;
    }


    /**
     * calls the open-meteo service to get the data for  each of specified location(s)
     * in the weather request one open-meteo request is required per location
     * @return
     * @throws IOException
     */
    public OpenStreetLocationResponse getLocationData(Location location) throws IOException {
        OpenStreetLocationResponse openStreetLocationResponse = new OpenStreetLocationResponse();
            try {
                openStreetLocationResponse = openStreetLocationHTTPRequest.makeOpenStreetLocationRequest(location);

            } catch (RestClientException e) {
                throw new IOException(e);
            }
            return openStreetLocationResponse;
    }

    /**
     * returns a OpenStreetLocationResponse object for a location object wih a given county and state
     * @param location the location object
     * @return OpenStreetLocationResponse object
     * @throws IOException
     */
    public OpenStreetLocationResponse getLocationDataByCountyAndState(Location location) throws IOException {
        OpenStreetLocationResponse openStreetLocationResponse = new OpenStreetLocationResponse();
        try {
            openStreetLocationResponse = openStreetLocationHTTPRequest.makeOpenStreetLocationRequestByCountyAndState(location);

        } catch (RestClientException e) {
            throw new IOException(e);
        }
        return openStreetLocationResponse;
    }
    /**
     * populates a location  object for a location object wih a given latitude and longitude
     * @param location the location object
     * @return
     * @throws IOException
     */
    private void populateLocationData(Location location, OpenStreetLocationResponse openStreetLocationResponse){
        if(location.getCounty()==null){
            location.setCounty(openStreetLocationResponse.getAddress().getCounty());
        }
        if(location.getState()==null){
            location.setState(openStreetLocationResponse.getAddress().getState());
        }
        if(location.getCity()==null){
            location.setCity(openStreetLocationResponse.getAddress().getCity());
        }
        if(location.getZipCode()==null){
            location.setZipCode(openStreetLocationResponse.getAddress().getPostcode());
        }
        if(location.getName()==null){
            String text="";
            if(location.getCity()!=null && !location.getCity().isEmpty()) {
                text=location.getCity()+", "+location.getState();
            }
            else {
                text = location.getCounty() + ", " + location.getState();
            }
            location.setName(text);
        }
    }

}
