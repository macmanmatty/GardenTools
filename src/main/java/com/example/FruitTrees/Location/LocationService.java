package com.example.FruitTrees.Location;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class LocationService {

    private  LocationRepository locationRepository;

    public LocationService() {

    }

    // Create a new location
    @Async
    public LocationDTO createLocation(LocationDTO locationDTO) {
        // Convert LocationDTO to entity if needed
        LocationEntity locationEntity = mapDTOToEntity(locationDTO);


        // Perform any additional business logic if necessary
        // ...

        // Save the entity
        LocationEntity savedEntity = locationRepository.save(locationEntity);

        // Convert the saved entity back to DTO and return
        return mapEntityToDTO(savedEntity);
    }

    // Retrieve a location by ID
    public LocationDTO getLocationById(Long id) throws EntityNotFoundException {
        // Retrieve the entity from the repository
        LocationEntity locationEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        // Convert the entity to DTO and return
        return mapEntityToDTO(locationEntity);
    }

    // Update a location by ID
    public LocationDTO updateLocation(Long id, LocationDTO locationDTO) throws EntityNotFoundException {
        // Check if the location with the given ID exists
        LocationEntity existingEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        // Update the entity with the new data from the DTO
        existingEntity.setName(locationDTO.getName());
        existingEntity.setLatitude(locationDTO.getLatitude());
        existingEntity.setLongitude(locationDTO.getLongitude());

        // Perform any additional business logic if necessary
        // ...

        // Save the updated entity
        LocationEntity updatedEntity = locationRepository.save(existingEntity);

        // Convert the updated entity to DTO and return
        return mapEntityToDTO(updatedEntity);
    }

    // Delete a location by ID
    public void deleteLocation(long id) throws EntityNotFoundException {
        // Check if the location with the given ID exists
        LocationEntity existingEntity = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));

        // Perform any additional business logic if necessary
        // ...

        // Delete the entity
        locationRepository.delete(existingEntity);
    }

    // Helper method to convert LocationDTO to LocationEntity
    private LocationEntity mapDTOToEntity(LocationDTO locationDTO) {
       LocationEntity locationEntity=new LocationEntity();
       locationEntity.setLatitude(locationDTO.getLatitude());
        locationEntity.setLongitude(locationDTO.getLongitude());
        locationEntity.setName(locationDTO.getName());
        locationEntity.setStationId(locationDTO.getStationId());
        return  locationEntity;
    }

    // Helper method to convert LocationEntity to LocationDTO
    private LocationDTO mapEntityToDTO(LocationEntity locationEntity) {
        LocationDTO locationDTO=new LocationDTO();
        locationDTO.setLatitude(locationEntity.getLatitude());
        locationDTO.setLongitude(locationEntity.getLongitude());
        locationDTO.setName(locationEntity.getName());
        locationDTO.setStationId(locationEntity.getStationId());
        return  locationDTO;
    }
}