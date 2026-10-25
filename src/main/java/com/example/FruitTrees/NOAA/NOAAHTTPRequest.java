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
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
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
    private final RateLimiter limiter;


    public NOAAHTTPRequest(
            CacheManager cacheManager,
            NoaaStationFinder noaaStationFinder,
            @Value("${noaa.api.rate-limit}") double rateLimit) {

        this.cacheManager = cacheManager;
        this.noaaStationFinder = noaaStationFinder;

        this.limiter = RateLimiter.create(rateLimit);
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
       {

        LocalDate startDate = LocalDate.parse(
                weatherRequest.getStartDate().toString()
        );

        LocalDate endDate = LocalDate.parse(
                weatherRequest.getEndDate().toString()
        );

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "NOAA start date must not be after end date"
            );
        }

        String stationId = location.getStationId();

        if (stationId == null || stationId.isBlank()) {
            stationId = noaaStationFinder.findNearestStation(
                    location.getLatitude(),
                    location.getLongitude(),
                    weatherRequest.getNoaaStationSearchRadius(),
                    weatherRequest.getStartDate(),
                    weatherRequest.getEndDate(),
                    hourlyWeatherProcessRequest.getHourlyDataType(),
                    NOAAStationType.fromString(
                            weatherRequest.getNoaaStationType()
                    )
            );
        }

        if (stationId == null || stationId.isBlank()) {
            throw new IllegalStateException(
                    "No NOAA station found near "
                            + location.getLatitude()
                            + ", "
                            + location.getLongitude()
            );
        }

        List<NOAAWeatherRecord> allData = new ArrayList<>();

        LocalDate chunkStart = startDate;

        while (!chunkStart.isAfter(endDate)) {

            LocalDate chunkEnd = chunkStart
                    .withDayOfYear(1)
                    .plusYears(1)
                    .minusDays(1);

            if (chunkEnd.isAfter(endDate)) {
                chunkEnd = endDate;
            }

            LOGGER.info(
                    "Fetching NOAA data for station " + stationId
                            + " from " + chunkStart
                            + " through " + chunkEnd
            );

            fetchDateRange(
                    stationId,
                    weatherRequest,
                    chunkStart,
                    chunkEnd,
                    allData
            );

            chunkStart = chunkEnd.plusDays(1);
        }

        NOAAHourlyDataMap result = new NOAAHourlyDataMap();
        result.addRecords(allData);

        return result;
    }

    /**
     * Retrieves every page of observations within one date chunk.
     */
    private void fetchDateRange(
            String stationId,
            WeatherRequest weatherRequest,
            LocalDate startDate,
            LocalDate endDate,
            List<NOAAWeatherRecord> allData) {

        int offset = 1;

        while (true) {

            String url = buildNoaaUrl(
                    stationId,
                    weatherRequest,
                    startDate,
                    endDate,
                    offset
            );

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

            NOAAResponse responseBody = Objects.requireNonNull(
                    response.getBody(),
                    "NOAA returned an empty response body"
            );

            List<NOAAWeatherRecord> results =
                    responseBody.getNoaaHourlyObservations();

            if (results == null || results.isEmpty()) {
                break;
            }

            allData.addAll(results);

            if (results.size() < PAGE_SIZE) {
                break;
            }

            offset += PAGE_SIZE;
        }
    }

    /**
     * Builds a CDO API v2 URL for one page of one date chunk.
     */
    private String buildNoaaUrl(
            String stationId,
            WeatherRequest weatherRequest,
            LocalDate startDate,
            LocalDate endDate,
            int offset) {

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString(noaaUrl)
                        .queryParam("datasetid", "global-hourly")
                        .queryParam("stationid", stationId)
                        .queryParam("startdate", startDate)
                        .queryParam("enddate", endDate)
                        .queryParam("units", weatherRequest.unit)
                        .queryParam("limit", PAGE_SIZE)
                        .queryParam("offset", offset);

        Set<String> dataTypes =
                weatherRequest.getHourlyDataTypes();

        if (dataTypes != null) {
            for (String datatype : dataTypes) {

                String noaaDatatype =
                        DataUtilities.toNOAADatatype(datatype);

                if (noaaDatatype != null
                        && !noaaDatatype.isBlank()) {

                    builder.queryParam(
                            "datatypeid",
                            noaaDatatype
                    );
                }
            }
        }

        return builder.build()
                .encode()
                .toUriString();
    }

}