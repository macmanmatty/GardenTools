package com.example.FruitTrees.WeatherConroller.WeatherResponse;

public enum WeatherRange {
  TOTAL("Total", 0),   YEARLY("Year", 1), MONTHLY("Month", 2),  DAILY("Day", 3);

    String name;
    int level;

    WeatherRange(String name, int level) {
        this.name = name;
        this.level = level;
    }
}
