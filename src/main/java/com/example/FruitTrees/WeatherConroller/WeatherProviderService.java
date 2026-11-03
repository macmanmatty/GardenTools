package com.example.FruitTrees.WeatherConroller;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.NOAA.NOAAService;
import com.example.FruitTrees.NOAA.NOAAStation;
import com.example.FruitTrees.NOAA.NoaaStationFinder;
import com.example.FruitTrees.OpenMeteo.LocationResponses;
import com.example.FruitTrees.OpenMeteo.OpenMeteoService;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import com.example.FruitTrees.WeatherProcessor.DataMode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class WeatherProviderService {

    private static final double MAX_STATION_DISTANCE_MILES = 25.0;
    private static final double PREFERRED_STATION_DISTANCE_MILES = 15.0;
    private static final double MAX_ELEVATION_DIFFERENCE_METERS = 150.0;

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
            WeatherRequest request) throws IOException, InterruptedException {

        Objects.requireNonNull(request, "WeatherRequest is required");

        DataMode provider = determineProvider(request);
        if (provider == DataMode.OPEN_METEO_ONLY ) {
            return fallback(responses, request);
        }

       else {
            // Find station once and assess its suitability.
            // Adapt this to your actual station finder return type.
            NOAAStation station = stationFinder.findBestStation(request);

            if (station == null) {
                return fallback(responses, request);
            }

            if (provider == DataMode.AUTO &&
                    !isSuitableStation(station)) {
                return fallback(responses, request);
            }

        }

        // Ideally pass station.getStationId() into NOAA getData
        // so it doesn't repeat station discovery.
        WeatherResponse result = noaaService.getData(responses, request);

        if ( result==null || !hasUsableData(result, request)) {
           result= fallback(responses, request);
            result.getWarnings().add("No NOAA Station Found For Location Falling Back to Open-Meteo ");

        }

        return result;
    }

    private DataMode determineProvider(WeatherRequest request) {

        // Future dates require a provider that supports them.
        if (LocalDate.parse(request.getEndDate()).isAfter(LocalDate.now())) {
            return DataMode.OPEN_METEO_ONLY;
        }

        // Existing boolean preference.
        if (!request.isUseNOAA()) {
            return DataMode.OPEN_METEO_ONLY;
        }

        // AUTO is the default for NOAA-preferred historical requests.
        return DataMode.AUTO;
    }

    private boolean isSuitableStation(NOAAStation station) {

        if (station.getDistanceMiles() > MAX_STATION_DISTANCE_MILES) {
            return false;
        }

        // Prefer closer stations. More distant stations must
        // have a reasonably similar elevation.
        if (station.getDistanceMiles() > PREFERRED_STATION_DISTANCE_MILES) {

            Double difference = station.getElevationDifferenceMeters();

            if (difference != null &&
                    difference > MAX_ELEVATION_DIFFERENCE_METERS) {
                return false;
            }
        }

        return true;
    }

    private boolean hasUsableData(
            WeatherResponse response,
            WeatherRequest request) {

        // Implement this against your actual response model.
        // A non-null response alone does not guarantee sufficient
        // NOAA observations for climate calculations.
        return response != null;
    }

    private WeatherResponse fallback(
            LocationResponses responses,
            WeatherRequest request) throws IOException {

        WeatherResponse weatherResponse= openMeteoService.getData(responses, request);
        if(weatherResponse == null) {
            throw new IOException("No Weather Provider Found For Location");
        }
        return weatherResponse;
    }
}
