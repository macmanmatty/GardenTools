package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.Location.Location;

import java.time.LocalDateTime;
import java.util.Map;

public interface LocationResponse {

   LocalDateTime[] getTime();
   Map<String, double[]> getData();
    Location getLocation();
    String getDataSource();

}
