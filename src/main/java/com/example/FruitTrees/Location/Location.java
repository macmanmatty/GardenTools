package com.example.FruitTrees.Location;

/**
 * class the represents a location
 */
public class Location {
    /**
     * the id of the location
     */
    private long id;
    /**
     * the name of the location
     */
   private   String name;
    /**
     * the latitude and longitude of the location
     */
   private  String latitude;
   private  String longitude;
    /**
    the weather station id currently not used
     */
   private  String stationId;

   private String streetName;

   private String streetNumber;

   private String stateAbbreviation;
   private String county;
   private  String zipCode;
   private  String city;
   private String state;
    public String suburb;
    public String borough;
    /**
     * the US FIPS Code For the state
     */
   private String stateFips;

    /**
     * the US FIPS Code For the county
     */
    private String countyFips;
    private String propertyType;
    private String propertyClass;
    private boolean populated=false;
   private LocationArea locationArea= new LocationArea();
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getCounty() {
        return county;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateFips() {
        return stateFips;
    }

    public void setStateFips(String stateFips) {
        this.stateFips = stateFips;
    }

    public String getCountyFips() {
        return countyFips;
    }

    public void setCountyFips(String countyFips) {
        this.countyFips = countyFips;
    }

    public LocationArea getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(LocationArea locationArea) {
        this.locationArea = locationArea;
    }

    public boolean isPopulated() {
        return populated;
    }

    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyClass() {
        return propertyClass;
    }

    public void setPropertyClass(String propertyClass) {
        this.propertyClass = propertyClass;
    }
}
