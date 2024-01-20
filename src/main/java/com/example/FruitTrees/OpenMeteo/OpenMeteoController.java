package com.example.FruitTrees.OpenMeteo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class OpenMeteoController {
    OpenMeteoService openMeteoService;

    @Autowired
    public OpenMeteoController(OpenMeteoService openMeteoService) {
        this.openMeteoService = openMeteoService;
    }

    @GetMapping("/weatherInfo")
    public ResponseEntity<WeatherResponse> getWeather(WeatherRequest weatherRequest) {
        try {
         WeatherResponse weatherResponse=   openMeteoService.getData(weatherRequest);
            return  new ResponseEntity<>(weatherResponse, HttpStatus.OK);

        } catch (IOException e) {
            return  new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
}
