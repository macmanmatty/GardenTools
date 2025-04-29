package com.example.FruitTrees.Location;
import com.example.FruitTrees.OpenStreetMap.Model.OpenStreetLocationResponse;
import com.example.FruitTrees.OpenStreetMap.OpenStreetLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class LocationController {

    private final LocationService locationService;

    private final OpenStreetLocationService openStreetLocationService;


    @Autowired
    public LocationController( @Autowired OpenStreetLocationService openStreetLocationService,  LocationService locationService) {
        this.locationService = locationService;
        this.openStreetLocationService=openStreetLocationService;
    }
    @PostMapping(value = "/locationInfo", consumes = {"application/json"})
    public ResponseEntity<Object> getLocationInfo( @RequestBody Location location) {
        try {
           OpenStreetLocationResponse locationResponse=   openStreetLocationService.getLocationDataByCountyAndState(location);
            return  new ResponseEntity<>(locationResponse, HttpStatus.OK);

        } catch (IOException e) {
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping(value = "/locationAddress")
    public ResponseEntity<Object> getLocationInfo( @RequestBody String address) {
        try {
            Location locationResponse =   locationService.getFullLocation(address);
            return  new ResponseEntity<>(locationResponse, HttpStatus.OK);

        } catch (IOException e) {
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping(value = "/propertyBounds", consumes = {"application/json"})
    public ResponseEntity<Object> getPropertyBounds( @RequestBody Location location) {
        try {
            location =locationService.getPropertyBounds(location);
            return  new ResponseEntity<>(location, HttpStatus.OK);

        } catch (IOException e) {
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    // Create a new location
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location createdLocation = locationService.createLocation(location);
        return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
    }

    // Retrieve a location by ID
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable Long id) throws EntityNotFoundException {
        Location location = locationService.getLocationById(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    // Update a location by ID
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) throws EntityNotFoundException {
        Location updatedLocation = locationService.updateLocation(id, location);
        return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
    }

    // Delete a location by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
