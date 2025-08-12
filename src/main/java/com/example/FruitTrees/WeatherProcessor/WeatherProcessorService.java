package com.example.FruitTrees.WeatherProcessor;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.OpenMeteo.LocationResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.WeatherProcessor;
import com.example.FruitTrees.OpenMeteo.LocationResponses;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.LocationWeatherResponse;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;


@Service
public class WeatherProcessorService {
    private static final Logger log = LoggerFactory.getLogger(WeatherProcessorService.class);
    WeatherProcessorFactory weatherProcessorFactory;
    public WeatherProcessorService(@Autowired WeatherProcessorFactory weatherProcessorFactory) {
        this.weatherProcessorFactory = weatherProcessorFactory;
    }
    /**
     *  processes hourly weather data
     * @param weatherRequest the weather request object
     * @param openMeteoResponses the data from the open meteo API call for each location specified
     *  in the WeatherRequest Object
     * @return WeatherResponse object containing all of the processed data from the processors
     */
    public WeatherResponse processHourlyData( WeatherRequest weatherRequest, LocationResponses openMeteoResponses){
        List<LocationResponse> locationResponses=openMeteoResponses.getLocationResponses();
        WeatherResponse weatherResponse = new WeatherResponse();
        for(LocationResponse locationResponse: locationResponses) {
          processLocationData(locationResponse, weatherRequest, weatherResponse);
        }
        return weatherResponse;
    }
    /**
     * processes the data for each individual location specified in the WeatherRequest Object
     * @param locationResponse the location response object holding the location object and
     *  open meteo data for that location
     * @param weatherRequest The WeatherRequest Object
     * @param weatherResponse  The WeatherResponse object to add all of  processed data from the processors to
     * @return WeatherResponse object containing all the processed data from the processors
     */
 private WeatherResponse   processLocationData(LocationResponse locationResponse, WeatherRequest weatherRequest, WeatherResponse weatherResponse){
     Location location =locationResponse.getLocation();
    LocationWeatherResponse locationWeatherResponse= new LocationWeatherResponse();
     locationWeatherResponse.setLocation(location);
     String name = locationResponse.getLocation().getName()+" At: "+"Lat: " + locationResponse.getLocation().getLatitude()+" Lon: " +locationResponse.getLocation().getLongitude();
     weatherResponse.getLocationWeatherResponses().put(name,locationWeatherResponse);
     String text="Values For Location: "+ location.getName();
     locationWeatherResponse.getLocationResponses().add(text);
     List<HourlyWeatherProcessRequest> hourlyWeatherProcessRequests = weatherRequest.getHourlyWeatherProcessRequests();
     LocalDateTime [] time = locationResponse.getTime();
     List<WeatherProcessor> weatherProcessors = new ArrayList<>();
     for (HourlyWeatherProcessRequest hourlyWeatherProcessRequest : hourlyWeatherProcessRequests) {
           WeatherProcessor weatherProcessor=  weatherProcessorFactory.createProcessor(hourlyWeatherProcessRequest, locationWeatherResponse);
         log.info(" started processing of {}", weatherProcessor.getProcessorName() +" for "+locationResponse.getLocation().getName());
         List<HourlyWeatherProcessRequest> dependentWeatherProcessors=weatherProcessor.getHourlyWeatherProcessRequests();
         for(HourlyWeatherProcessRequest dependentWeatherProcessorRequest:dependentWeatherProcessors){
             WeatherProcessor dependentWeatherProcessor=  weatherProcessorFactory.createProcessor(dependentWeatherProcessorRequest, locationWeatherResponse);
             weatherProcessors.add(dependentWeatherProcessor);
         }
         weatherProcessors.add(weatherProcessor);
     }
     processHourlyWeather( time, weatherProcessors,locationResponse.getData());

     return weatherResponse;
 }


    public void processHourlyWeather(
            LocalDateTime [] iso8601Times,
            List<WeatherProcessor> processors,
            Map<String, double []> seriesByType
    ) {
        final int sampleCount = iso8601Times.length;
        // De-dup & keep order stable
        List<WeatherProcessor> activeProcessors = new ArrayList<>(new LinkedHashSet<>(processors));

        // Run before() once per processor
        for (WeatherProcessor weatherProcessor : activeProcessors) {
            weatherProcessor.before();
        }

        // Walk hours once; fan out to processors
        for (int hourIndex = 0; hourIndex < sampleCount && !activeProcessors.isEmpty(); hourIndex++) {
            final LocalDateTime timestamp = iso8601Times[hourIndex];

            for (int p = 0; p < activeProcessors.size(); ) {
                WeatherProcessor weatherProcessor = activeProcessors.get(p);

                // Pull this processor's series; skip if missing or ragged
                double [] series = seriesByType.get(weatherProcessor.getDataType());
                if (series == null || hourIndex >= series.length) { p++; continue; }

                double value = series[hourIndex];

                // If you add date-window logic, check here before processing:
                // if (!weatherProcessor.dateRule().accepts(localDateFrom(timestamp))) { p++; continue; }

                weatherProcessor.processWeatherExternal(value, timestamp);

                // If you add an "isDone()" flag, you can prune here:
                // if (weatherProcessor.isDone()) { activeProcessors.remove(p); } else { p++; }
                p++;
            }
        }

        // Finalize once per processor
        for (WeatherProcessor processor : activeProcessors) {
            processor.after();
            if (processor.isCalculateMeanAverage())  processor.calculateMeanAverageValue();
            if (processor.isCalculateMedianAverage()) processor.calculateMedianAverageValue();
            if (processor.isCalculateMin())           processor.calculateMinValue();
            if (processor.isCalculateMax())           processor.calculateMaxValue();

            List<String> text = processor.getProcessedTextValues();
            processor.getLocationWeatherResponse().getLocationResponses().addAll(text);
        }
    }

}
