package com.example.FruitTrees.SoilProcessor;

import com.example.FruitTrees.File.FileSaver;
import com.example.FruitTrees.OpenMeteo.OpenMeteoService;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SoilProcessorController {
    OpenMeteoService openMeteoService;

    @Autowired
    public SoilProcessorController(OpenMeteoService openMeteoService) {
        this.openMeteoService = openMeteoService;
    }

    @PostMapping(value = "/soilInfo", consumes = {"application/json"})
    public ResponseEntity<Object> getWeather( @RequestBody SoilRequest soilRequest) {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


}
