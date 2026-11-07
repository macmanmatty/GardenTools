package com.example.FruitTrees.NOAA;
import com.example.FruitTrees.Location.Location;
import com.example.FruitTrees.Utilities.DataUtilities;
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
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
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
    private final RestTemplate restTemplate = new RestTemplate();
    private final CacheManager cacheManager;
    private final NoaaStationFinder noaaStationFinder;
    private NOAAHourlyNormalizer noaaHourlyNormalizer;
    /*
     * NOAA currently permits more than two requests per second,
     * but keeping this below NOAA's maximum provides some margin.
     */
    private final RateLimiter limiter;
    public NOAAHTTPRequest(
            CacheManager cacheManager,
            NoaaStationFinder noaaStationFinder, NOAAHourlyNormalizer noaaHourlyNormalizer,
            @Value("${noaa.api.rate-limit}") double rateLimit) {
        this.cacheManager = cacheManager;
        this.noaaStationFinder = noaaStationFinder;
        this.limiter = RateLimiter.create(rateLimit);
        this.noaaHourlyNormalizer = noaaHourlyNormalizer;
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
            WeatherRequest weatherRequest)
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
        NOAAStation station = location.getNoaaStation();


        String stationId=station.getStationId();
        if (stationId == null || stationId.isBlank()) {
            return null;
        }
           System.err.println(">>> STATION FINDER RETURNED: " + stationId);
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
            location.setNoaaStation(station);
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
           noaaHourlyNormalizer.normalize(
                   result,
                   startDate,
                   endDate
           );
           return result;
    }
    /**
     * Fetches NOAA Global Hourly observations for a date range.
     *
     * Converts the returned raw NOAA records into individual weather
     * measurements and appends them to the supplied collection.
     *
     * @param stationId NOAA station identifier
     * @param weatherRequest request containing data types and units
     * @param startDate beginning of the date range
     * @param endDate end of the date range
     * @param allData destination collection for converted observations
     */
    private void fetchDateRange(
            String stationId,
            WeatherRequest weatherRequest,
            LocalDate startDate,
            LocalDate endDate,
            List<NOAAWeatherRecord> allData) {
        String url = buildNoaaUrl(
                stationId,
                weatherRequest,
                startDate,
                endDate
        );
        boolean useFahrenheit = true;
        if (weatherRequest.temperatureUnit.equalsIgnoreCase("celsius") || weatherRequest.temperatureUnit.equalsIgnoreCase("c")) {
        useFahrenheit = false;
        }
        limiter.acquire();
        URI uri = URI.create(url);
        LOGGER.info("NOAA REQUEST: " + uri);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<NOAAGlobalHourlyRecord[]> response =
                restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        entity,
                        NOAAGlobalHourlyRecord[].class
                );
        NOAAGlobalHourlyRecord[] results = Objects.requireNonNull(
                response.getBody(),
                "NOAA returned an empty response body"
        );
        for (NOAAGlobalHourlyRecord record : results) {
            allData.addAll(convertRecord(record, useFahrenheit));
        }
        LOGGER.info("Retrieved " + results.length
                + " NOAA observations for " + startDate
                + " through " + endDate);
    }
    /**
     * Extracts supported measurements from a raw NOAA observation.
     *
     * Each raw observation may contain multiple meteorological fields.
     * Currently converts temperature (TMP) and dew point (DEW).
     *
     * @param raw raw NOAA Global Hourly observation
     * @param useFahrenheit whether to convert temperatures to Fahrenheit
     * @return converted measurements from the raw observation
     */
    private List<NOAAWeatherRecord> convertRecord(
            NOAAGlobalHourlyRecord raw, boolean useFahrenheit) {
        List<NOAAWeatherRecord> records = new ArrayList<>();
        addMeasurement(
                records,
                raw,
                "TMP",
                raw.getTemperature(),
                10.0,
                9999, useFahrenheit
        );
        addMeasurement(
                records,
                raw,
                "DEW",
                raw.getDewPoint(),
                10.0,
                9999, useFahrenheit
        );
        return records;
    }
    /**
     * Decodes and validates a NOAA temperature measurement.
     *
     * NOAA encodes temperature and dew point as comma-separated values,
     * with the numeric measurement stored in tenths of a degree Celsius.
     *
     * Measurements with missing values or disallowed quality flags
     * are discarded.
     *
     * @param records destination list for valid measurements
     * @param raw original NOAA observation
     * @param datatype NOAA data type identifier
     * @param encodedValue raw encoded measurement
     * @param divisor scaling divisor applied to the encoded value
     * @param missingValue NOAA missing-value sentinel
     * @param useFahrenheit whether to convert Celsius to Fahrenheit
     */
    private void addMeasurement(
            List<NOAAWeatherRecord> records,
            NOAAGlobalHourlyRecord raw,
            String datatype,
            String encodedValue,
            double divisor,
            int missingValue,  boolean useFahrenheit) {
        if (encodedValue == null || encodedValue.isBlank()) {
            return;
        }
        String[] parts = encodedValue.split(",");
        if (parts.length < 2) {
            return;
        }
        try {
            int encoded = Integer.parseInt(parts[0]);
            if (Math.abs(encoded) == missingValue) {
                return;
            }
            String qualityFlag = parts[1];
            if (!qualityFlag.equals("1")
                    && !qualityFlag.equals("5")) {
                return;
            }
            NOAAWeatherRecord record = new NOAAWeatherRecord();
            record.setDate(raw.getDate());
            record.setStation(raw.getStation());
            record.setDatatype(datatype);
            record.setValue(encoded / divisor);
            record.setAttributes(qualityFlag);
            if(useFahrenheit) {
                double fahrenheit = (encoded / 10.0) * 9.0 / 5.0 + 32.0;
                record.setValue(fahrenheit);
            }
            records.add(record);
        } catch (NumberFormatException e) {
            LOGGER.warning(
                    "Invalid NOAA " + datatype + " value: " + encodedValue
            );
        }
    }
    /**
     * Builds the NOAA Global Hourly API URL.
     *
     * Converts requested application data types into NOAA identifiers,
     * removes duplicates, and includes the station and date range.
     *
     * @param stationId NOAA station identifier
     * @param weatherRequest weather request containing data types
     * @param startDate beginning of the requested date chunk
     * @param endDate end of the requested date chunk
     * @return NOAA API request URL
     */
    private String buildNoaaUrl(
            String stationId,
            WeatherRequest weatherRequest,
            LocalDate startDate,
            LocalDate endDate) {
        Set<String> requestedTypes =
                weatherRequest.getHourlyDataTypes();
        HashSet<String> dataTypes =  new LinkedHashSet<String>();
        if (requestedTypes != null) {
            for (String datatype : requestedTypes) {
                String noaaDatatype =
                        DataUtilities.toNOAADatatype(datatype);
                if (noaaDatatype != null && !noaaDatatype.isBlank()) {
                    dataTypes.add(noaaDatatype);
                }
            }
        }
        if (dataTypes.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one NOAA hourly data type is required"
            );
        }
        return UriComponentsBuilder
                .fromUriString(noaaUrl)
                .queryParam("dataset", "global-hourly")
                .queryParam("stations", stationId)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .queryParam("dataTypes", String.join(",", dataTypes))
                .queryParam("format", "json")
                .build()
                .toUriString();
    }
}