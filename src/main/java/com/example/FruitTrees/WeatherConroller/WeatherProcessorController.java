
package com.example.FruitTrees.WeatherConroller;

import com.example.FruitTrees.OpenMeteo.LocationResponses;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class WeatherProcessorController {

    private static final Logger log =
            LoggerFactory.getLogger(WeatherProcessorController.class);

    private final WeatherProviderService weatherProviderService;

    public WeatherProcessorController(
            WeatherProviderService weatherProviderService) {

        this.weatherProviderService = weatherProviderService;
    }

    /**
     * Retrieves weather data using the appropriate provider.
     * NOAA is preferred when configured and suitable;
     * otherwise Open-Meteo is used.
     */
    @PostMapping(
            value = "/weatherInfo",
            consumes = "application/json"
    )
    public ResponseEntity<?> getWeather(
            @RequestBody WeatherRequest weatherRequest) {

        try {
            LocationResponses locationResponses =
                    new LocationResponses();

            WeatherResponse weatherResponse =
                    weatherProviderService.getWeather(
                            locationResponses,
                            weatherRequest
                    );

            return ResponseEntity.ok(weatherResponse);

        }
        catch (BeanCreationException e) {
            log.error("Failed to create weather processor", e);

            return ResponseEntity
                    .badRequest()
                    .body("Invalid Weather Processor Supplied");
        }
        catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            log.error("Weather request interrupted", e);

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Weather request interrupted");

        } catch (IllegalArgumentException e) {

            log.warn("Invalid weather request: {}", e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (IOException e) {

            log.error("Weather provider request failed", e);

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body("Unable to retrieve weather data");

        } catch (Exception e) {

            log.error("Unexpected weather processing error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal weather processing error");
        }
    }
}
