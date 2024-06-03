package com.example.FruitTrees.OpenStreetLocation;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.LocationUtilities;
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
     * populates data for  location  object for a location object wih a given latitude and longitude
     * if location does not have a name gives it one based on the state and county or city
     * @param location the location object
     */
    private void populateLocationData(Location location, OpenStreetLocationResponse openStreetLocationResponse){
            location.setCounty(openStreetLocationResponse.getAddress().getCounty());
             location.setState(openStreetLocationResponse.getAddress().getState());
            location.setCity(openStreetLocationResponse.getAddress().getCity());
          location.setZipCode(openStreetLocationResponse.getAddress().getPostcode());
          if(location.getState()!=null) {
              location.setStateFips(LocationUtilities.getStateFipsCode(location.getState()));
          }
        if(location.getCounty()!=null) {
            location.setCountyFips(LocationUtilities.getStateFipsCode(location.getState()));
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
