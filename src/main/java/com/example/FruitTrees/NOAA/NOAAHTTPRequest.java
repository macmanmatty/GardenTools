package com.example.FruitTrees.NOAA;

import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.DataUtilities;
import com.example.FruitTrees.WeatherConroller.HourlyWeatherProcessRequest;
import com.example.FruitTrees.WeatherConroller.WeatherRequest;
import com.google.common.util.concurrent.RateLimiter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Service responsible for retrieving hourly weather observations
 * from NOAA/NCEI.
 *
 * This service:
 *
 * 1. Determines the NOAA station to use for the requested location.
 * 2. Builds the NOAA data request.
 * 3. Handles NOAA pagination.
 * 4. Applies rate limiting between NOAA API requests.
 * 5. Converts the returned observations into NOAAHourlyDataMap.
 */
@Service
public class NOAAHTTPRequest {

    private static final Logger LOGGER =
            Logger.getLogger(NOAAHTTPRequest.class.getName());

    private static final int PAGE_SIZE = 1000;

    @Value("${noaa.url}")
    private String noaaUrl;

    @Value("${noaa.api.key}")
    private String noaaApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final CacheManager cacheManager;
    private final NoaaStationFinder noaaStationFinder;

    /*
     * NOAA currently permits more than two requests per second,
     * but keeping this below NOAA's maximum provides some margin.
     */
    private final RateLimiter limiter = RateLimiter.create(2.0);

    public NOAAHTTPRequest(
            CacheManager cacheManager,
            NoaaStationFinder noaaStationFinder) {

        this.cacheManager = cacheManager;
        this.noaaStationFinder = noaaStationFinder;
    }

    /**
     * Retrieves NOAA hourly weather observations for a location.
     *
     * If the Location already contains a NOAA station ID, that station
     * is used. Otherwise the nearest appropriate station is located.
     *
     * NOAA limits the number of observations returned in a single
     * response, so requests are paginated until all observations have
     * been retrieved.
     *
     * @param location location for which weather data is requested
     * @param weatherRequest request containing date range, search radius,
     *                       and requested hourly data types
     * @param hourlyWeatherProcessRequest information about the hourly
     *                                    weather calculation being performed
     *
     * @return NOAA hourly observations organized in a NOAAHourlyDataMap
     *
     * @throws InterruptedException retained for compatibility with
     *                              the existing method signature
     */
    @Cacheable(
            value = "noaaDataCache",
            key = "#location.getLatitude() + ':'"
                    + " + #location.getLongitude() + ':'"
                    + " + #weatherRequest.getHourlyDataTypes().hashCode() + ':'"
                    + " + #weatherRequest.getStartDate() + ':'"
                    + " + #weatherRequest.getEndDate()"
    )
    public NOAAHourlyDataMap makeLocationRequest(
            Location location,
            WeatherRequest weatherRequest,
            HourlyWeatherProcessRequest hourlyWeatherProcessRequest)
            throws InterruptedException {

        List<NOAAWeatherRecord> allData = new ArrayList<>();

        /*
         * NOAA offsets are record offsets rather than page numbers.
         * Start with the first record.
         */
        int offset = 1;

        String stationId = location.getStationId();

        /*
         * If a NOAA station has not already been assigned to this
         * location, locate the nearest suitable station.
         */
        if (stationId == null || stationId.isBlank()) {

            stationId = noaaStationFinder.findNearestStation(
                    location.getLatitude(),
                    location.getLongitude(),
                    weatherRequest.getNoaaStationSearchRadius(),
                    weatherRequest.getStartDate(),
                    weatherRequest.getEndDate(),
                    hourlyWeatherProcessRequest.getHourlyDataType()
            );
        }

        /*
         * Do not make a NOAA request if no suitable station could
         * be found.
         */
        if (stationId == null || stationId.isBlank()) {
            throw new IllegalStateException(
                    "No NOAA station found near "
                            + location.getLatitude()
                            + ", "
                            + location.getLongitude()
            );
        }

        boolean moreData = true;

        while (moreData) {

            String url = buildNoaaUrl(
                    stationId,
                    weatherRequest,
                    offset
            );

            LOGGER.info("NOAA station ID: " + stationId);
            LOGGER.info("NOAA URL: " + url);

            /*
             * Wait for a rate-limit permit BEFORE making the HTTP
             * request. This is important because the request itself
             * is the operation being rate limited.
             */
            limiter.acquire();

            HttpHeaders headers = new HttpHeaders();
            headers.set("token", noaaApiKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<NOAAResponse> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            NOAAResponse.class
                    );

            NOAAResponse responseBody =
                    Objects.requireNonNull(
                            response.getBody(),
                            "NOAA returned an empty response body"
                    );

            List<NOAAWeatherRecord> results =
                    responseBody.getNoaaHourlyObservations();

            /*
             * No observations means there are no additional pages.
             */
            if (results == null || results.isEmpty()) {
                moreData = false;
                continue;
            }

            allData.addAll(results);

            /*
             * A partial page indicates that this was the final page.
             *
             * If NOAA returned a complete page, advance the offset
             * by PAGE_SIZE and request the next page.
             */
            if (results.size() < PAGE_SIZE) {
                moreData = false;
            } else {
                offset += PAGE_SIZE;
            }
        }

        NOAAHourlyDataMap noaaHourlyDataMap =
                new NOAAHourlyDataMap();

        noaaHourlyDataMap.addRecords(allData);

        return noaaHourlyDataMap;
    }

    /**
     * Builds the NOAA data API URL for a single page of observations.
     *
     * Multiple datatypeid parameters may be supplied. NOAA interprets
     * repeated datatypeid parameters as requests for multiple weather
     * variables.
     */
    private String buildNoaaUrl(
            String stationId,
            WeatherRequest weatherRequest,
            int offset) {

        StringBuilder url = new StringBuilder(noaaUrl);

        url.append("?datasetid=global-hourly")
                .append("&stationid=").append(stationId)
                .append("&startdate=").append(weatherRequest.getStartDate())
                .append("&enddate=").append(weatherRequest.getEndDate())
                .append("&units=standard")
                .append("&limit=").append(PAGE_SIZE)
                .append("&offset=").append(offset);

        Set<String> dataTypes =
                weatherRequest.getHourlyDataTypes();

        if (dataTypes != null) {
            for (String datatype : dataTypes) {

                String noaaDatatype =
                        DataUtilities.toNOAADatatype(datatype);

                if (noaaDatatype != null
                        && !noaaDatatype.isBlank()) {

                    url.append("&datatypeid=")
                            .append(noaaDatatype);
                }
            }
        }

        return url.toString();
    }
}