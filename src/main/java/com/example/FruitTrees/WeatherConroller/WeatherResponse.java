package com.example.FruitTrees.WeatherConroller;
import com.example.FruitTrees.Location.Location;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class WeatherResponse {
  public List<String> responses = new ArrayList<>();
  public List<LocationWeatherResponse> locationWeatherResponses= new ArrayList<>();
    public List<String> getResponses() {
        return responses;
    }
    public void setResponses(List<String> responses) {
        this.responses = responses;
    }
    public static class LocationWeatherResponse{
       public  List<SubWeatherValues> yearlyValues = new ArrayList<>();
        Map<String, String> locationTotals =new HashMap<>();
        public Location location;
        
        public static class SubWeatherValues {
            String name="";
            Map<String, String> values= new HashMap<>();
            public  List<SubWeatherValues> monthlyWeatherValues=new ArrayList<>();
        }
    }
}
