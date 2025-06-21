package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;
import com.example.FruitTrees.OpenMeteo.LocationResponses;
import com.example.FruitTrees.OpenStreetMap.OpenStreetLocationService;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.RequestValidation;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessorService;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Service
public class NOAAService {
   private final NOAAHTTPRequest noaahttpRequest;
   private  final WeatherProcessorService weatherProcessorService;
   private final OpenStreetLocationService openStreetLocationService;
    private final RequestValidation requestValidation;
   @Autowired
    public NOAAService( NOAAHTTPRequest noaahttpRequest, WeatherProcessorService weatherProcessorService,
                       OpenStreetLocationService openStreetLocationService, RequestValidation requestValidation) {
        this.noaahttpRequest = noaahttpRequest;
        this.weatherProcessorService = weatherProcessorService;
        this.openStreetLocationService=openStreetLocationService;
        this.requestValidation = requestValidation;
    }
    public WeatherResponse getData(LocationResponses locationResponses, WeatherRequest weatherRequest ) throws IOException, InterruptedException {
        extractHourlyDataTypes(weatherRequest);
        LocationResponses noaaRespnses=makeRequest(locationResponses, weatherRequest);
        WeatherResponse  response= weatherProcessorService.processHourlyData( weatherRequest, noaaRespnses);
        return  response;
    }
    /**
     * extracts the   hourly  data types  from the HourlyWeatherProcessRequest objects
     * @param weatherRequest
     */
    private void extractHourlyDataTypes( WeatherRequest weatherRequest) {
        List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests=weatherRequest.getHourlyWeatherProcessRequests();
        weatherRequest.getHourlyDataTypes().clear();
        for(HourlyWeatherProcessRequest hourlyWeatherProcessRequest:hourlyWeatherProcessRequests){
            weatherRequest.getHourlyDataTypes().add(hourlyWeatherProcessRequest.getHourlyDataType());
        }
    }
    /**
     * calls the open-meteo service to get the data for  each of specified location(s)
     * in the weather request one open-meteo request is required per location
     * @param weatherRequest the weather request objcet
     * @return
     * @throws IOException
     */
    private LocationResponses makeRequest( LocationResponses locationResponses, WeatherRequest weatherRequest) throws IOException, InterruptedException {
        List<Location> locations =weatherRequest.getLocations();
        requestValidation.locationCheck(locations);
        boolean populateLocationData= weatherRequest.isPopulateLocationData();
        NOAALocationResponse noaaLocationResponse= new NOAALocationResponse();
        for (Location location : locations) {
            try {
                for(HourlyWeatherProcessRequest weatherProcessor: weatherRequest.hourlyWeatherProcessRequests) {
                    NOAAHourlyDataMap noaaHourlyDataMap = noaahttpRequest.makeLocationRequest(location, weatherRequest, weatherProcessor);
                    noaaLocationResponse.getNoaaHourlyDataMap().getNoaaHourlyObservationsMap().putAll(noaaHourlyDataMap.getNoaaHourlyObservationsMap());
                }
                    if (populateLocationData) {
                        openStreetLocationService.populateLocationData(location);
                    }
                locationResponses.getLocationResponses().add(noaaLocationResponse);

            } catch (RestClientException e) {
                Logger.getLogger("").info(e.toString());

                throw new IOException(e);
            }
        }
        return  locationResponses;
    }

}
