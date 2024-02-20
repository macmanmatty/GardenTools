package com.example.FruitTrees.WeatherConroller.WeatherResponse;

import java.util.HashMap;
import java.util.Map;

public class DailyValues {
        private String name="";
        private Map<String, String> values= new HashMap<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getValues() {
            return values;
        }

        public void setValues(Map<String, String> values) {
            this.values = values;
        }


}
