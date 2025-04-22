package com.example.FruitTrees.Location;
import com.example.FruitTrees.OpenStreetLocation.OpenStreetLocationService;
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

    }


    public Location getPropertyBounds(Location location) throws IOException {
        location = openStreetLocationService.forwardGeocodeAddress(location);
        location=regridService.populateLocationCoordinatesAsPropertyBounds(location);
        return location;
    }

    // Create a new location
    public LocationDTO createLocation(LocationDTO locationDTO) {
        // Convert LocationDTO to entity if needed
        Location locationEntity = mapDTOToEntity(locationDTO);


        // Perform any additional business logic if necessary
        // ...

        // Save the entity
        Location savedEntity = locationRepository.save(locationEntity);

        // Convert the saved entity back to DTO and return
        return mapEntityToDTO(savedEntity);
    }

    // Retrieve a location by ID
    public LocationDTO getLocationById(Long id) throws EntityNotFoundException {
        // Retrieve the entity from the repository
        Location locationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        // Convert the entity to DTO and return
        return mapEntityToDTO(locationEntity);
    }

    // Update a location by ID
    public LocationDTO updateLocation(Long id, LocationDTO locationDTO) throws EntityNotFoundException {
        // Check if the location with the given ID exists
        Location existingEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        // Update the entity with the new data from the DTO
        existingEntity.setName(locationDTO.getName());
        existingEntity.setLatitude(locationDTO.getLatitude());
        existingEntity.setLongitude(locationDTO.getLongitude());

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
    private Location mapDTOToEntity(LocationDTO locationDTO) {
       Location locationEntity=new Location();
       locationEntity.setLatitude(locationDTO.getLatitude());
        locationEntity.setLongitude(locationDTO.getLongitude());
        locationEntity.setName(locationDTO.getName());
        locationEntity.setStationId(locationDTO.getStationId());
        return  locationEntity;
    }

    // Helper method to convert LocationEntity to LocationDTO
    private LocationDTO mapEntityToDTO(Location locationEntity) {
        LocationDTO locationDTO=new LocationDTO();
        locationDTO.setLatitude(locationEntity.getLatitude());
        locationDTO.setLongitude(locationEntity.getLongitude());
        locationDTO.setName(locationEntity.getName());
        locationDTO.setStationId(locationEntity.getStationId());
        return  locationDTO;
    }
}