package com.example.FruitTrees.OpenMeteo;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenStreetMap.OpenStreetLocationService;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.RequestValidation;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessorService;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesCalculator;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.DerivedSeries.DerivedSeriesRegistry;
import jdk.jfr.DataAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import java.io.IOException;
import java.util.*;

@Service
public class OpenMeteoService {
   private final  OpenMeteoHTTPRequest openMeteoHTTPRequest;
   private  final WeatherProcessorService weatherProcessorService;
   private final OpenStreetLocationService openStreetLocationService;
    private final RequestValidation requestValidation;
    DerivedSeriesRegistry derivedSeriesRegistry;
    @Value("${stream-data}")
    boolean streamData;
    @Autowired
    public OpenMeteoService(OpenMeteoHTTPRequest openMeteoHTTPRequest, WeatherProcessorService weatherProcessorService,
   OpenStreetLocationService openStreetLocationService, RequestValidation requestValidation, DerivedSeriesRegistry derivedSeriesRegistry) {
        this.openMeteoHTTPRequest = openMeteoHTTPRequest;
        this.weatherProcessorService = weatherProcessorService;
        this.openStreetLocationService=openStreetLocationService;
        this.requestValidation = requestValidation;
        this.derivedSeriesRegistry = derivedSeriesRegistry;
    }
    public WeatherResponse getData( LocationResponses locationResponses, WeatherRequest weatherRequest ) throws IOException {
        extractHourlyDataTypes(weatherRequest);
        LocationResponses openMeteoResponses=makeRequest(locationResponses, weatherRequest);
        WeatherResponse  response= weatherProcessorService.processHourlyData( weatherRequest, openMeteoResponses);
        return  response;
    }
    /**
     * extracts the   hourly  data types  from the HourlyWeatherProcessRequest objects
     * @param weatherRequest
     */
    private void extractHourlyDataTypes(WeatherRequest weatherRequest) {
        List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests=weatherRequest.getHourlyWeatherProcessRequests();
        weatherRequest.getHourlyDataTypes().clear();
        for(HourlyWeatherProcessRequest hourlyWeatherProcessRequest:hourlyWeatherProcessRequests){
            weatherRequest.getHourlyDataTypes().add(hourlyWeatherProcessRequest.getHourlyDataType());
        }
      weatherRequest.setOpenmeteoRequestHourlyDataTypes(plan(weatherRequest.getHourlyDataTypes(), weatherRequest.bestEffort()).fetchVars);
    }

    public record Plan(Set<String> fetchVars, Set<String> derivedKeys) {}

    public Plan plan(Set<String> requestedKeys, boolean bestEffort) {
        // 1) Expand dependencies to figure out what we might need
        Set<String> allKeysNeeded = expandKeys(requestedKeys, bestEffort);

        // 2) Decide what we must fetch (raw leaves), and what we will compute
        Set<String> fetchVars = new LinkedHashSet<>();
        Set<String> derivedToCompute = new LinkedHashSet<>();

        for (String key : allKeysNeeded) {
            DerivedSeriesCalculator calc = derivedSeriesRegistry.getCalculatorOrNull(key);

            if (calc == null) {
                // Not derived => must come from Open-Meteo (or already in map)
                String openMeteoVar = DataUtilities.toOpenMeteoDatatype(key);
                if (openMeteoVar != null) fetchVars.add(openMeteoVar);
                continue;
            }

            // Derived key: decide compute vs fetch if Open-Meteo also supports it
            String openMeteoVar = DataUtilities.toOpenMeteoDatatype(key);
            boolean openMeteoSupportsIt = (openMeteoVar != null);

            // If we'd rather compute it and we already plan to fetch its deps, compute it.
            if ( depsAreFetchable(calc, allKeysNeeded)) {
                derivedToCompute.add(key);
            } else if (openMeteoSupportsIt ) {
                // prefer fetching if Open-Meteo has it and we didn't choose derived
                fetchVars.add(openMeteoVar);
            } else {
                // otherwise compute it
                derivedToCompute.add(key);
            }
        }

        return new Plan(fetchVars, derivedToCompute);
    }

    private Set<String> expandKeys(Set<String> requestedKeys, boolean bestEffort) {
        Set<String> out = new LinkedHashSet<>();
        Deque<String> stack = new ArrayDeque<>(requestedKeys);

        while (!stack.isEmpty()) {
            String key = stack.pop();
            if (!out.add(key)) continue;

            DerivedSeriesCalculator calc = derivedSeriesRegistry.getCalculatorOrNull(key);
            if (calc == null) continue;

            stack.addAll(calc.requiredInputTypes());
            if (bestEffort) stack.addAll(calc.optionalInputTypes());
        }
        return out;
    }

    private boolean depsAreFetchable(DerivedSeriesCalculator calc, Set<String> allKeysNeeded) {
        // Simple interpretation: deps exist in overall plan; they’ll be fetched or derived.
        // If you want strictly “fetched from Open-Meteo”, tighten this check.
        for (String dep : calc.requiredInputTypes()) {
            if (!allKeysNeeded.contains(dep)) return false;
        }
        return true;
    }

    /**
     * calls the open-meteo service to get the data for  each of specified location(s)
     * in the weather request one open-meteo request is required per location
     * @param weatherRequest the weather request objcet
     * @return Location Response
     * @throws IOException
     */
    private LocationResponses makeRequest( LocationResponses locationResponses, WeatherRequest weatherRequest) throws IOException {

        List<Location> locations =weatherRequest.getLocations();
        requestValidation.locationCheck(locations);
        boolean populateLocationData= weatherRequest.isPopulateLocationData();
        for (Location location : locations) {
            try {
                    OpenMeteoLocationResponse locationResponse = openMeteoHTTPRequest.makeLocationRequest(location, weatherRequest);
                    locationResponses.getLocationResponses().add(locationResponse);

                if(populateLocationData){
                    openStreetLocationService.populateLocationData(location);
                }
            } catch (RestClientException e) {
                throw new IOException(e);
            }
        }
        return  locationResponses;
    }




}
