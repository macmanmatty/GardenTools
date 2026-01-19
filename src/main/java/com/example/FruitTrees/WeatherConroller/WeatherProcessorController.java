package com.example.FruitTrees.WeatherConroller;

import com.example.FruitTrees.File.FileSaver;
import com.example.FruitTrees.Metrics.RequestUnitNormalizer;
import com.example.FruitTrees.NOAA.NOAAService;
import com.example.FruitTrees.OpenMeteo.LocationResponses;
import com.example.FruitTrees.OpenMeteo.OpenMeteoService;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class WeatherProcessorController {
    private static final Logger log = LoggerFactory.getLogger(WeatherProcessorController.class);
    OpenMeteoService openMeteoService;
    @Autowired
    NOAAService noaaService;
    @Autowired
    public WeatherProcessorController(OpenMeteoService openMeteoService) {
        this.openMeteoService = openMeteoService;
    }

    @PostMapping(value = "/weatherInfo", consumes = {"application/json"})
    public ResponseEntity<Object> getWeather( @RequestBody  WeatherRequest weatherRequest) {
        try {
            RequestUnitNormalizer.normalizeHourlyConfigsToCanonical(weatherRequest);
            LocationResponses locationResponses= new LocationResponses();
            WeatherResponse weatherResponse;
            if(weatherRequest.isUseNOAA()) {
               weatherResponse = noaaService.getData(locationResponses, weatherRequest);
            }
            else{
                weatherResponse = openMeteoService.getData(locationResponses, weatherRequest);
            }

         if(weatherRequest.isSaveToFile()){
             FileSaver.saveFile(weatherRequest.getFilePath(), weatherRequest.getOutputFileType(), weatherResponse);
         }
            return  new ResponseEntity<>(weatherResponse, HttpStatus.OK);

     }
        catch (IllegalArgumentException e) {
            // Bad user input: unknown units, invalid thresholds, etc.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
      catch (IOException | InterruptedException e) {
      return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
        catch(BeanCreationException e){
            return  new ResponseEntity<>("Invalid Weather Processor Supplied", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unhandled error in /weatherInfo", e);
            return  new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
