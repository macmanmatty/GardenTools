package com.example.FruitTrees.Regrid;

import com.example.FruitTrees.Location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

@Service
public class RegridService {
    private final RegridHTTPRequest regridHTTPRequest;
   @Autowired
    public RegridService(RegridHTTPRequest regridHTTPRequest) {
       this.regridHTTPRequest = regridHTTPRequest;
    }

    /**
     * calls the open-meteo service to get the data for  each of specified location(s)
     * in the weather request one open-meteo request is required per location
     * @return
     * @throws IOException
     */
    public Location populateLocationCoordinatesAsPropertyBounds(Location location) throws IOException {
            try {
               location = regridHTTPRequest.makeRegridLocationRequest(location);
            } catch (RestClientException e) {
                throw new IOException(e);
            }
            return location;
    }

}
