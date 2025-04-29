package com.example.FruitTrees.Location;
import com.example.FruitTrees.OpenStreetMap.Model.OpenStreetLocationResponse;
import com.example.FruitTrees.OpenStreetMap.OpenStreetLocationService;
import com.example.FruitTrees.Regrid.RegridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public class LocationService {

    private  LocationRepository locationRepository;
    private OpenStreetLocationService openStreetLocationService;
    private RegridService regridService;

    @Autowired
    public LocationService(OpenStreetLocationService openStreetLocationService,  RegridService regridService) {
        this.openStreetLocationService = openStreetLocationService;
        this.regridService = regridService;
    }

    public Location getFullLocation(String address) throws IOException {
        Location location = new Location();
        OpenStreetLocationResponse openStreetLocationResponse=openStreetLocationService.forwardGeocodeAddress(address);
        if(openStreetLocationResponse!=null) {
            location.setLongitude(openStreetLocationResponse.getLon());
            location.setLatitude(openStreetLocationResponse.getLat());
            location.setCity(openStreetLocationResponse.getAddress().getCity());
            location.setState(openStreetLocationResponse.getAddress().getState());
            location.setStreetNumber(openStreetLocationResponse.getAddress().getHouse_number());
            location.setStreetName(openStreetLocationResponse.getAddress().getRoad());
            location.setBorough(openStreetLocationResponse.getAddress().getBorough());
            location.setSuburb(openStreetLocationResponse.getAddress().getSuburb());
            location.setPropertyType(openStreetLocationResponse.getType());
            location.setPropertyClass(openStreetLocationResponse.getOsmClass());
            location.setPopulated(true);
        }
        return location;
    }


    public Location getPropertyBounds(Location location) throws IOException {
        location =regridService.populateLocationCoordinatesAsPropertyBounds(location);
        return location;
    }

    // Create a new location
    public Location createLocation(Location location) {
        // Convert LocationDTO to entity if needed
        Location locationEntity = mapDTOToEntity(location);


        // Perform any additional business logic if necessary
        // ...

        // Save the entity
        Location savedEntity = locationRepository.save(locationEntity);

        // Convert the saved entity back to DTO and return
        return mapEntityToDTO(savedEntity);
    }

    // Retrieve a location by ID
    public Location getLocationById(Long id) throws EntityNotFoundException {
        // Retrieve the entity from the repository
        Location locationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        // Convert the entity to DTO and return
        return mapEntityToDTO(locationEntity);
    }

    // Update a location by ID
    public Location updateLocation(Long id, Location location) throws EntityNotFoundException {
        // Check if the location with the given ID exists
        Location existingEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        // Update the entity with the new data from the DTO
        existingEntity.setName(location.getName());
        existingEntity.setLatitude(location.getLatitude());
        existingEntity.setLongitude(location.getLongitude());

        // Perform any additional business logic if necessary
        // ...

        // Save the updated entity
        Location updatedEntity = locationRepository.save(existingEntity);

        // Convert the updated entity to DTO and return
        return mapEntityToDTO(updatedEntity);
    }

    // Delete a location by ID
    public void deleteLocation(long id) throws EntityNotFoundException {
        // Check if the location with the given ID exists
        Location existingEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        // Perform any additional business logic if necessary
        // ...

        // Delete the entity
        locationRepository.delete(existingEntity);
    }

    // Helper method to convert LocationDTO to LocationEntity
    private Location mapDTOToEntity(Location location) {
       Location locationEntity =new Location();
       locationEntity.setLatitude(location.getLatitude());
        locationEntity.setLongitude(location.getLongitude());
        locationEntity.setName(location.getName());
        locationEntity.setStationId(location.getStationId());
        return locationEntity;
    }

    // Helper method to convert LocationEntity to LocationDTO
    private Location mapEntityToDTO(Location locationEntity) {
        Location location =new Location();
        location.setLatitude(locationEntity.getLatitude());
        location.setLongitude(locationEntity.getLongitude());
        location.setName(locationEntity.getName());
        location.setStationId(locationEntity.getStationId());
        return location;
    }
}