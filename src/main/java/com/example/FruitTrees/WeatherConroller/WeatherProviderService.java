
package com.example.FruitTrees.WeatherConroller;

import com.example.FruitTrees.NOAA.NOAAService;
import com.example.FruitTrees.NOAA.NOAAStation;
import com.example.FruitTrees.NOAA.NoaaStationFinder;
import com.example.FruitTrees.OpenMeteo.LocationResponses;
import com.example.FruitTrees.OpenMeteo.OpenMeteoService;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import com.example.FruitTrees.WeatherProcessor.ProviderMode;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class WeatherProviderService {

    private final NOAAService noaaService;
    private final OpenMeteoService openMeteoService;
    private final NoaaStationFinder stationFinder;

    public WeatherProviderService(
            NOAAService noaaService,
            OpenMeteoService openMeteoService,
            NoaaStationFinder stationFinder) {

        this.noaaService = noaaService;
        this.openMeteoService = openMeteoService;
        this.stationFinder = stationFinder;
    }

    /**
     * Selects the weather provider and retrieves weather data.
     *
     * NOAA stations are selected independently for each
     * requested data type.
     *
     * User-selected stations bypass automatic suitability checks.
     */
    public WeatherResponse getWeather(
            LocationResponses responses,
            WeatherRequest request)
            throws IOException, InterruptedException {

        Objects.requireNonNull(
                request,
                "WeatherRequest is required"
        );

        Objects.requireNonNull(
                request.getLocation(),
                "Weather location is required"
        );

        ProviderMode provider = determineProvider(request);

        if (provider == ProviderMode.OPEN_METEO_ONLY) {
            return getOpenMeteo(responses, request);
        }

        // Preserve stations explicitly supplied by the user.
        Map<String, NOAAStation> suppliedStations =
                request.getLocation().getNoaaStations();

        // The finder handles:
        // 1. Global NOAA station ID override.
        // 2. User-selected stations per data type.
        // 3. Automatic discovery for remaining types.
        Map<String, NOAAStation> stations =
                stationFinder.findBestStation(request);

        if (stations == null || stations.isEmpty()) {
            return fallback(
                    request,
                    "No suitable NOAA stations found"
            );
        }

        Map<String, NOAAStation> suitableStations =
                new LinkedHashMap<>();

        String globalStationId = request.getLocation().getNoaaStationId();

        boolean globalOverride =
                globalStationId != null
                        && !globalStationId.isBlank();

        for (Map.Entry<String, NOAAStation> entry
                : stations.entrySet()) {

            String dataType = entry.getKey();
            NOAAStation station = entry.getValue();

            if (station == null
                    || station.getStationId() == null
                    || station.getStationId().isBlank()) {
                continue;
            }

            // A global station ID overrides all automatic selection.
            if (globalOverride) {
                suitableStations.put(dataType, station);
                continue;
            }

            // Explicitly supplied per-type stations bypass
            // distance and elevation restrictions.
            boolean userSelected = isUserSelected(
                    dataType,
                    station,
                    suppliedStations
            );

            if (userSelected
                    || isSuitableStation(station, request)) {

                suitableStations.put(dataType, station);
            }
        }

        if (suitableStations.isEmpty()) {
            return fallback(
                    request,
                    "No NOAA stations meet the selection criteria"
            );
        }

        request.getLocation().setNoaaStations(suitableStations);

        WeatherResponse result =
                noaaService.getData(responses, request);

        if (result == null) {
            return fallback(
                    request,
                    "NOAA returned no weather data"
            );
        }

        return result;
    }

    /**
     * Determines whether a station was explicitly
     * selected for a particular data type.
     */
    private boolean isUserSelected(
            String dataType,
            NOAAStation station,
            Map<String, NOAAStation> suppliedStations) {

        if (suppliedStations == null
                || suppliedStations.isEmpty()) {
            return false;
        }

        for (Map.Entry<String, NOAAStation> entry
                : suppliedStations.entrySet()) {

            if (entry.getKey() == null
                    || !entry.getKey().equalsIgnoreCase(dataType)) {
                continue;
            }

            NOAAStation supplied = entry.getValue();

            return supplied != null
                    && supplied.getStationId() != null
                    && supplied.getStationId().equals(
                    station.getStationId()
            );
        }

        return false;
    }

    /**
     * Preserves the existing provider-selection behavior.
     */
    private ProviderMode determineProvider(
            WeatherRequest request) {

        if (!request.isUseNOAA()) {
            return ProviderMode.OPEN_METEO_ONLY;
        }

        LocalDate endDate =
                LocalDate.parse(request.getEndDate());

        if (endDate.isAfter(LocalDate.now())) {
            return ProviderMode.OPEN_METEO_ONLY;
        }

        return ProviderMode.AUTO;
    }

    /**
     * Checks whether an automatically discovered
     * station satisfies distance and elevation limits.
     */
    private boolean isSuitableStation(
            NOAAStation station,
            WeatherRequest request) {

        if (station.getDistanceMiles()
                > request.getNoaaStationSearchRadius()) {
            return false;
        }

        double locationElevation =
                request.getLocation().getElevation();

        if (locationElevation == Double.MIN_VALUE
                || !Double.isFinite(locationElevation)) {
            return true;
        }

        Double difference =
                station.getElevationDifferenceMeters(
                        locationElevation
                );

        if (difference == null) {
            return true;
        }

        return difference <=
                request.getMaxNoaaStationElevationDifference();
    }

    /**
     * Falls back to Open-Meteo without retaining
     * rejected NOAA station selections.
     */
    private WeatherResponse fallback(
            WeatherRequest request,
            String reason) throws IOException {

        request.getLocation().setNoaaStations(null);

        WeatherResponse result = getOpenMeteo(
                new LocationResponses(),
                request
        );

        if (result.getWarnings() != null) {
            result.getWarnings().add(
                    reason + ". Falling back to Open-Meteo."
            );
        }

        return result;
    }

    private WeatherResponse getOpenMeteo(
            LocationResponses responses,
            WeatherRequest request) throws IOException {

        WeatherResponse result =
                openMeteoService.getData(responses, request);

        if (result == null) {
            throw new IOException(
                    "No weather data available from Open-Meteo"
            );
        }

        return result;
    }
}
