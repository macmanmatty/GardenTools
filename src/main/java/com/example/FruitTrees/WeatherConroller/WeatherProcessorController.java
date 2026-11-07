package com.example.FruitTrees.WeatherConroller;
import com.example.FruitTrees.File.FileSaver;
import com.example.FruitTrees.OpenMeteo.LocationResponses;
import com.example.FruitTrees.OpenMeteo.OpenMeteoService;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
@RestController
public class WeatherProcessorController {
    WeatherProviderService weatherProviderService;
    @Autowired
    public WeatherProcessorController(WeatherProviderService openMeteoService) {
    }
    @PostMapping(value = "/weatherInfo", consumes = {"application/json"})
    public ResponseEntity<Object> getWeather( @RequestBody  WeatherRequest weatherRequest) {
        try {
            LocationResponses locationResponses= new LocationResponses();
             WeatherResponse weatherResponse = weatherProviderService.getWeather(locationResponses, weatherRequest);
            return  new ResponseEntity<>(weatherResponse, HttpStatus.OK);
     }
      catch (InterruptedException | IllegalArgumentException e) {
      return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
        catch(IOException e ){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
        catch(BeanCreationException e){
            return  new ResponseEntity<>("Invalid Weather Processor Supplied", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            {
                return  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
