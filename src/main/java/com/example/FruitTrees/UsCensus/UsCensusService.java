package com.example.FruitTrees.UsCensus;

import com.example.FruitTrees.UsCensus.Request.USCensusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
@Service
public class UsCensusService {
    private final UsCensusHTTPRequest usCensusHTTPRequest;
   @Autowired
    public UsCensusService(UsCensusHTTPRequest usCensusHTTPRequest) {
       this.usCensusHTTPRequest = usCensusHTTPRequest;
    }

    /**
     * calls the open-meteo service to get the data for  each of specified location(s)
     * in the weather request one open-meteo request is required per location
     * @return
     * @throws IOException
     */
    public UsCensusResponse getCensusData(USCensusRequest usCensusRequest) throws IOException {
        UsCensusResponse usCensusResponse = new UsCensusResponse();
            try {
                usCensusResponse = usCensusHTTPRequest.makeUsCensusRequest(usCensusRequest);
            } catch (RestClientException e) {
                throw new IOException(e);
            }
            return usCensusResponse;
    }

}
