package com.example.FruitTrees.WeatherProcessor.WeatherProcessors;

import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RequiredExternalData {
    List<WeatherProcessor> getRequiredProcessors=  new ArrayList<>();
    public  void setRequiredProcessors(List<WeatherProcessor> requiredData);

}
