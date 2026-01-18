package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Observation;


import com.example.FruitTrees.WeatherProcessor.Period;
import com.example.FruitTrees.WeatherProcessor.Stat;

import java.util.Map;

public record Observation(
  String locationId,   // UUID, stable internal identity
Integer year,
  Integer month,
  String metricId,
 Value value,
  String unit,
  Stat stat,
  Period period,
  Map<String,Object> meta


) {}
