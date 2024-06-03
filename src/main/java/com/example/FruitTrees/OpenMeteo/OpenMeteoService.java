package com.example.FruitTrees.OpenMeteo;
import com.example.FruitTrees.OpenStreetLocation.OpenStreetLocationService;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessorService;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.WeatherConroller.BadRequestException;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import java.io.IOException;
import java.util.List;
@Service
public class OpenMeteoService {
   private final  OpenMeteoHTTPRequest openMeteoHTTPRequest;
   private  final WeatherProcessorService weatherProcessorService;
   private final OpenStreetLocationService openStreetLocationService;
    @Value("${enable.Multiple.Location.Processing}")
    private  boolean processMultipleLocationRequestsEnabled;
    @Value("${max.Multiple.OpenMeteo.Requests}")
    private  int maxMultipleOpenMeteoRequests;
   @Autowired
    public OpenMeteoService(OpenMeteoHTTPRequest openMeteoHTTPRequest, WeatherProcessorService weatherProcessorService,
   OpenStreetLocationService openStreetLocationService) {
        this.openMeteoHTTPRequest = openMeteoHTTPRequest;
        this.weatherProcessorService = weatherProcessorService;
        this.openStreetLocationService=openStreetLocationService;
    }
    public WeatherResponse getData(WeatherRequest weatherRequest ) throws IOException {
        extractHourlyDataTypes(weatherRequest);
        OpenMeteoLocationResponses openMeteoResponses=makeRequest(weatherRequest);
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
    }
    /**
     * calls the open-meteo service to get the data for  each of specified location(s)
     * in the weather request one open-meteo request is required per location
     * @param weatherRequest the weather request objcet
     * @return
     * @throws IOException
     */
    private OpenMeteoLocationResponses makeRequest(WeatherRequest weatherRequest) throws IOException {
        OpenMeteoLocationResponses openMeteoResponses=  new OpenMeteoLocationResponses();
        List<Location> locations=weatherRequest.getLocations();
        locationCheck(locations);
        boolean populateLocationData= weatherRequest.isPopulateLocationData();
        for (Location location: locations) {
            try {
                LocationResponse locationResponse= openMeteoHTTPRequest.makeLocationRequest(location, weatherRequest);
                openMeteoResponses.getLocationResponses().add(locationResponse);
                if(populateLocationData){
                    openStreetLocationService.populateLocationData(location);
                }
            } catch (RestClientException e) {
                throw new IOException(e);
            }
        }
        return  openMeteoResponses;
    }
    /**
     * checks to make sure the number of locations
     * you are attempting to retrieve  data for is allowed
     * @param locations the list of locations
     */
    private void locationCheck(List<Location> locations) {
        int size=locations.size();
        if (size==0){
            throw new BadRequestException("No Locations Specified");
        }
        if(size>1 && !processMultipleLocationRequestsEnabled){
            throw new BadRequestException("You may only process one location at time currently");
        }
        else if(size>maxMultipleOpenMeteoRequests){
            throw new BadRequestException("You may only process "+maxMultipleOpenMeteoRequests+ " locations at time currently");
        }
    }
}
