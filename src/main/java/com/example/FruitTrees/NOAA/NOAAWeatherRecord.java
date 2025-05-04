package com.example.FruitTrees.NOAA;

public class NOAAWeatherRecord {
        private String date;
        private String datatype;
        private String station;
        private double value;
        private String attributes;

        // Getters and Setters
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public String getDatatype() { return datatype; }
        public void setDatatype(String datatype) { this.datatype = datatype; }

        public String getStation() { return station; }
        public void setStation(String station) { this.station = station; }

        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }

        public String getAttributes() { return attributes; }
        public void setAttributes(String attributes) { this.attributes = attributes; }

}
