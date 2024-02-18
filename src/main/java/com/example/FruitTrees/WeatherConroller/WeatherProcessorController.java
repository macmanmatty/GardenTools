package com.example.FruitTrees.WeatherConroller;

import com.example.FruitTrees.OpenMeteo.OpenMeteoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class WeatherProcessorController {
    OpenMeteoService openMeteoService;

    @Autowired
    public WeatherProcessorController(OpenMeteoService openMeteoService) {
        this.openMeteoService = openMeteoService;
    }

    @PostMapping(value = "/weatherInfo", consumes = {"application/json"})
    public ResponseEntity<Object> getWeather( @RequestBody  WeatherRequest weatherRequest) {
        try {
         WeatherResponse weatherResponse=   openMeteoService.getData(weatherRequest);
            return  new ResponseEntity<>(weatherResponse, HttpStatus.OK);

        } catch (IOException e) {
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {

            return  new ResponseEntity<>("Success", HttpStatus.OK);


    }
}
