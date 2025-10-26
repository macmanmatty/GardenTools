package com.example.FruitTrees.OpenMeteo;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenStreetMap.OpenStreetLocationService;
import com.example.FruitTrees.WeatherConroller.RequestValidation;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessorService;
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
    private final RequestValidation requestValidation;
    @Value("${stream-data}")
    boolean streamData;
    @Autowired
    public OpenMeteoService(OpenMeteoHTTPRequest openMeteoHTTPRequest, WeatherProcessorService weatherProcessorService,
   OpenStreetLocationService openStreetLocationService, RequestValidation requestValidation) {
        this.openMeteoHTTPRequest = openMeteoHTTPRequest;
        this.weatherProcessorService = weatherProcessorService;
        this.openStreetLocationService=openStreetLocationService;
        this.requestValidation = requestValidation;
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
