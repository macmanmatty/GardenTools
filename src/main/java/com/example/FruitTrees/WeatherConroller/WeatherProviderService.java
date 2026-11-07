
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
import java.util.Objects;

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

    public WeatherResponse getWeather(
            LocationResponses responses,
            WeatherRequest request)
            throws IOException, InterruptedException {

        Objects.requireNonNull(request, "WeatherRequest is required");
        Objects.requireNonNull(
                request.getLocation(),
                "Weather location is required"
        );



        ProviderMode provider = determineProvider(request);

        if (provider == ProviderMode.OPEN_METEO_ONLY) {
            return getOpenMeteo(responses, request);
        }

        NOAAStation station = request.getLocation().getNoaaStation();

        boolean userSelectedStation =
                station != null
                        && station.getStationId() != null
                        && !station.getStationId().isBlank();

        if (!userSelectedStation) {
            station = stationFinder.findBestStation(request);

            if (station == null) {
                return fallback(request, "No suitable NOAA station found");
            }
        }

        if (provider == ProviderMode.AUTO
                && !userSelectedStation
                && !isSuitableStation(station, request)) {

            return fallback(
                    request,
                    "NOAA station exceeds distance or elevation limits"
            );
        }
        request.getLocation().setNoaaStation(station);

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

    private boolean isSuitableStation(
            NOAAStation station,
            WeatherRequest request) {

        if (station.getDistanceMiles()
                > request.getNoaaStationSearchRadius()) {
            return false;
        }

        double locationElevation =
                request.getLocation().getElevation();

        // Retain your existing sentinel convention.
        // Prefer NaN or nullable Double in the future.
        if (locationElevation == Double.MIN_VALUE
                || !Double.isFinite(locationElevation)) {
            return true;
        }

        Double difference =
                station.getElevationDifferenceMeters(
                        locationElevation
                );

        if (difference == null) {
            // No station elevation available.
            // Allow NOAA, but elevation suitability
            // cannot be verified.
            return true;
        }

        return difference <=
                request.getMaxNoaaStationElevationDifference();
    }

    private WeatherResponse fallback(
            WeatherRequest request,
            String reason) throws IOException {

        // Avoid exposing a rejected NOAA station
        // as the station used for the final result.
        request.getLocation().setNoaaStation(null);

        // Fresh accumulator prevents partial NOAA
        // output from contaminating Open-Meteo.
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
