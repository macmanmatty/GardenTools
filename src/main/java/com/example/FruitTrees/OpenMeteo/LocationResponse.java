package com.example.FruitTrees.OpenMeteo;

import com.example.FruitTrees.Location.Location;

import java.util.List;

public interface LocationResponse {

   List<String> getTime();
   List<? extends Number> getData(String dataType);
    Location getLocation();
    String getDataSource();

}
