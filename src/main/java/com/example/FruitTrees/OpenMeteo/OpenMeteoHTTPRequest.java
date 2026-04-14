package com.example.FruitTrees.OpenMeteo;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherProcessorController;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.WeatherResponse;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Service class responsible for making Open-Meteo HTTP requests.
 *
 * IMPORTANT ARCHITECTURE RULE:
 *  - We request Open-Meteo in DEFAULT (scientific) units.
 *  - We do NOT append any canonicalUnit conversion params to the API call.
 *  - User canonicalUnit preferences are applied only at the boundaries:
 *      - normalize request thresholds/bounds to canonical before processing
 *      - convert results to user units for presentation/export
 *
 * This keeps the core engine canonicalUnit-clean and avoids conversion bugs.
 */
@Service
public class OpenMeteoHTTPRequest {
    @Value("${open-meteo.url}")
   private  String openMeteoUrl;

   private final  RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(OpenMeteoHTTPRequest.class);

    @Autowired
    public OpenMeteoHTTPRequest( RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Calls Open-Meteo for a single location over the request date range.
     *
     * Open-Meteo requires one request per location.
     *
     * NOTE: This method requests DEFAULT units only (no canonicalUnit parameters).
     */
    @Cacheable(
            value = "openMeteoDataCache",
            key = "T(java.lang.String).valueOf(#location.latitude) + ':' + " +
                    "T(java.lang.String).valueOf(#location.longitude) + ':' + " +
                    "#weatherRequest.startDate + ':' + #weatherRequest.endDate + ':' + " +
                    "T(java.util.Objects).hash(#root.target.buildHourlyParam(#weatherRequest))"
    )
    public OpenMeteoLocationResponse makeLocationRequest(Location location, WeatherRequest weatherRequest) {

        // Build hourly parameter as a stable, comma-separated string
        // (sorted so caching doesn't break due to ordering differences)
        String hourlyParam = buildHourlyParam(weatherRequest);

        UriComponentsBuilder b = UriComponentsBuilder.fromUriString(openMeteoUrl)
                .queryParam("latitude", location.getLatitude())
                .queryParam("longitude", location.getLongitude())
                .queryParam("start_date", weatherRequest.getStartDate())
                .queryParam("end_date", weatherRequest.getEndDate());

        if (!hourlyParam.isEmpty()) {
            // Open-Meteo expects ONE hourly param with comma-separated variables
            b.queryParam("hourly", hourlyParam);
        }

        // IMPORTANT: We intentionally do NOT add temperature_unit/wind_speed_unit/etc.

        URI uri = b.build(true).toUri();

        log.info("Getting weather data from Open-Meteo: " + uri);

        ResponseEntity<OpenMeteoResponse> response = restTemplate.getForEntity(uri, OpenMeteoResponse.class);
        OpenMeteoResponse body = response.getBody();

        if (body == null) {
            throw new IllegalStateException("Null Open-Meteo response for " + uri);
        }

        OpenMeteoLocationResponse locationResponse = new OpenMeteoLocationResponse();
        locationResponse.setOpenMeteoResponse(body);
        locationResponse.setLocation(location);

        log.info("Obtained weather data from Open-Meteo");

        return locationResponse;
    }

    /**
     * Builds a stable, comma-separated "hourly" parameter value.
     *
     * We:
     *  - map internal/request metric ids to Open-Meteo variable names
     *  - de-duplicate
     *  - sort for deterministic caching
     *
     * Java 8 friendly (no List.of(), no stream().toList()).
     */
   public  String buildHourlyParam(WeatherRequest weatherRequest) {
        Set<String> hourlyDataTypes = weatherRequest.getOpenmeteoRequestHourlyDataTypes();
        if (hourlyDataTypes == null || hourlyDataTypes.isEmpty()) {
            return "";
        }

        List<String> vars = new ArrayList<>(hourlyDataTypes.size());
        for (String dt : hourlyDataTypes) {
            String v = DataUtilities.toOpenMeteoDatatype(dt);
            if (v != null && !v.trim().isEmpty() && !vars.contains(v)) {
                vars.add(v);
            }
        }

        Collections.sort(vars);

        return String.join(",", vars);
    }


    /**
     *  adds additional params to the url to specify the units
     * you wish to have data sent back in Fahrenheit, Celsius, Inches , Meters Etc.
     * @param openMeteoUrl the request url for open meteo
     * @param weatherRequest the weather request object
     * @return
     */
    public String addConversionUnits(String openMeteoUrl, WeatherRequest weatherRequest){
        String temperatureUnit=weatherRequest.getTemperatureUnit();
        String windSpeedUnit=weatherRequest.getWindSpeedUnit();
        String precipitationUnit=weatherRequest.getPrecipitationUnit();
        if(temperatureUnit!=null && !temperatureUnit.isEmpty()) {
         openMeteoUrl=openMeteoUrl+   "&temperature_unit=" + weatherRequest.getTemperatureUnit();
        }
            if(windSpeedUnit!=null && !windSpeedUnit.isEmpty()) {
              openMeteoUrl=openMeteoUrl+  "&wind_speed_unit=" + weatherRequest.getWindSpeedUnit();
            }
            if(precipitationUnit!=null && !precipitationUnit.isEmpty()) {
              openMeteoUrl=openMeteoUrl+  "&precipitation_unit=" + weatherRequest.getPrecipitationUnit();
            }
         return    openMeteoUrl;
  }
}
