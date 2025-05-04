package com.example.FruitTrees.WeatherConroller;

import com.example.FruitTrees.Location.Location;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class RequestValidation {

    @Value("${enable.Multiple.Location.Processing}")
   private static  boolean processMultipleLocationRequestsEnabled;
    @Value("${max.Multiple.OpenMeteo.Requests}")
    private  static  int maxMultipleOpenMeteoRequests;
    /**
     * checks to make sure the number of locations
     * you are attempting to retrieve  data for is allowed
     * @param locations the list of locations
     */
    public static void locationCheck(List<Location> locations) {
        int size= locations.size();
        if (size==0){
            throw new BadRequestException("No Locations Specified");
        }
        if(size>1 && !processMultipleLocationRequestsEnabled){
            throw new BadRequestException("You may only process one location at time currently");
        }
        else if(size>maxMultipleOpenMeteoRequests){
            throw new BadRequestException("You may only process "+maxMultipleOpenMeteoRequests+ " locations at time currently");
        }
    }

    public static boolean isProcessMultipleLocationRequestsEnabled() {
        return processMultipleLocationRequestsEnabled;
    }

    public static int getMaxMultipleOpenMeteoRequests() {
        return maxMultipleOpenMeteoRequests;
    }
}
