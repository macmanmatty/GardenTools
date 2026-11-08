
package com.example.FruitTrees.OpenStreetMap;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenStreetMap.Model.OpenStreetLocationResponse;
import com.example.FruitTrees.Utilities.LocationUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Objects;

@Service
public class OpenStreetLocationService {

    private static final Logger log =
            LoggerFactory.getLogger(OpenStreetLocationService.class);

    private final OpenStreetLocationHTTPRequest httpRequest;

    public OpenStreetLocationService(
            OpenStreetLocationHTTPRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * Retrieves geographic information and populates
     * the supplied Location object.
     */
    public Location populateLocationData(Location location)
            throws IOException {

        Objects.requireNonNull(location, "Location is required");

        OpenStreetLocationResponse response =
                getLocationData(location);

        if (response == null || response.getAddress() == null) {
            throw new IOException(
                    "OpenStreetMap returned no address information"
            );
        }

        populateLocationData(location, response);

        return location;
    }

    /**
     * Reverse-geocodes a location using its coordinates.
     */
    public OpenStreetLocationResponse getLocationData(
            Location location) throws IOException {

        return executeRequest(
                () -> httpRequest.makeOpenStreetLocationRequest(location)
        );
    }

    /**
     * Looks up a location by county and state.
     */
    public OpenStreetLocationResponse getLocationDataByCountyAndState(
            Location location) throws IOException {

        return executeRequest(
                () -> httpRequest
                        .makeOpenStreetLocationRequestByCountyAndState(location)
        );
    }

    /**
     * Forward-geocodes a street address or place name.
     */
    public OpenStreetLocationResponse forwardGeocodeAddress(
            String address) throws IOException {

        return executeRequest(
                () -> httpRequest.forwardGeocodeAddress(address)
        );
    }

    /**
     * Executes an HTTP request and preserves the response
     * details when the remote service rejects it.
     */
    private OpenStreetLocationResponse executeRequest(
            LocationRequest operation) throws IOException {

        try {
            return operation.execute();

        } catch (HttpStatusCodeException e) {

            log.error(
                    "OpenStreetMap request failed. HTTP {}. Response: {}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );

            throw new IOException(
                    "OpenStreetMap returned HTTP "
                            + e.getStatusCode(),
                    e
            );

        } catch (RestClientException e) {

            log.error("OpenStreetMap request failed", e);

            throw new IOException(
                    "Unable to retrieve OpenStreetMap location data",
                    e
            );
        }
    }

    @FunctionalInterface
    private interface LocationRequest {
        OpenStreetLocationResponse execute();
    }

    /**
     * Copies address information into the Location object.
     * Preserves an existing location name.
     */
    private void populateLocationData(
            Location location,
            OpenStreetLocationResponse response) {

        var address = response.getAddress();

        location.setCounty(address.getCounty());
        location.setState(address.getState());
        location.setCity(address.getCity());
        location.setZipCode(address.getPostcode());

        if (location.getState() != null) {
            location.setStateFips(
                    LocationUtilities.getStateFipsCode(
                            location.getState()
                    )
            );
        }

        // County FIPS must be calculated separately.
        // Do not assign the state FIPS as the county FIPS.

        if (location.getName() == null
                || location.getName().isBlank()) {

            String place = location.getCity();

            if (place == null || place.isBlank()) {
                place = location.getCounty();
            }

            if (place != null && !place.isBlank()) {
                String state = location.getState();

                location.setName(
                        state == null || state.isBlank()
                                ? place
                                : place + ", " + state
                );
            }
        }
    }
}
