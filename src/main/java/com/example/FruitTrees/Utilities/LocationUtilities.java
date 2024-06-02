package com.example.FruitTrees.Utilities;

import java.util.HashMap;
import java.util.Map;

public class LocationUtilities {

    private LocationUtilities() {
    }

    private static final Map<String, String> stateFipsMap = new HashMap<>();

        static {
            // Adding state names and abbreviations to the map
            stateFipsMap.put("Alabama", "01");
            stateFipsMap.put("AL", "01");
            stateFipsMap.put("Alaska", "02");
            stateFipsMap.put("AK", "02");
            stateFipsMap.put("Arizona", "04");
            stateFipsMap.put("AZ", "04");
            stateFipsMap.put("Arkansas", "05");
            stateFipsMap.put("AR", "05");
            stateFipsMap.put("California", "06");
            stateFipsMap.put("CA", "06");
            stateFipsMap.put("Colorado", "08");
            stateFipsMap.put("CO", "08");
            stateFipsMap.put("Connecticut", "09");
            stateFipsMap.put("CT", "09");
            stateFipsMap.put("Delaware", "10");
            stateFipsMap.put("DE", "10");
            stateFipsMap.put("Florida", "12");
            stateFipsMap.put("FL", "12");
            stateFipsMap.put("Georgia", "13");
            stateFipsMap.put("GA", "13");
            stateFipsMap.put("Hawaii", "15");
            stateFipsMap.put("HI", "15");
            stateFipsMap.put("Idaho", "16");
            stateFipsMap.put("ID", "16");
            stateFipsMap.put("Illinois", "17");
            stateFipsMap.put("IL", "17");
            stateFipsMap.put("Indiana", "18");
            stateFipsMap.put("IN", "18");
            stateFipsMap.put("Iowa", "19");
            stateFipsMap.put("IA", "19");
            stateFipsMap.put("Kansas", "20");
            stateFipsMap.put("KS", "20");
            stateFipsMap.put("Kentucky", "21");
            stateFipsMap.put("KY", "21");
            stateFipsMap.put("Louisiana", "22");
            stateFipsMap.put("LA", "22");
            stateFipsMap.put("Maine", "23");
            stateFipsMap.put("ME", "23");
            stateFipsMap.put("Maryland", "24");
            stateFipsMap.put("MD", "24");
            stateFipsMap.put("Massachusetts", "25");
            stateFipsMap.put("MA", "25");
            stateFipsMap.put("Michigan", "26");
            stateFipsMap.put("MI", "26");
            stateFipsMap.put("Minnesota", "27");
            stateFipsMap.put("MN", "27");
            stateFipsMap.put("Mississippi", "28");
            stateFipsMap.put("MS", "28");
            stateFipsMap.put("Missouri", "29");
            stateFipsMap.put("MO", "29");
            stateFipsMap.put("Montana", "30");
            stateFipsMap.put("MT", "30");
            stateFipsMap.put("Nebraska", "31");
            stateFipsMap.put("NE", "31");
            stateFipsMap.put("Nevada", "32");
            stateFipsMap.put("NV", "32");
            stateFipsMap.put("New Hampshire", "33");
            stateFipsMap.put("NH", "33");
            stateFipsMap.put("New Jersey", "34");
            stateFipsMap.put("NJ", "34");
            stateFipsMap.put("New Mexico", "35");
            stateFipsMap.put("NM", "35");
            stateFipsMap.put("New York", "36");
            stateFipsMap.put("NY", "36");
            stateFipsMap.put("North Carolina", "37");
            stateFipsMap.put("NC", "37");
            stateFipsMap.put("North Dakota", "38");
            stateFipsMap.put("ND", "38");
            stateFipsMap.put("Ohio", "39");
            stateFipsMap.put("OH", "39");
            stateFipsMap.put("Oklahoma", "40");
            stateFipsMap.put("OK", "40");
            stateFipsMap.put("Oregon", "41");
            stateFipsMap.put("OR", "41");
            stateFipsMap.put("Pennsylvania", "42");
            stateFipsMap.put("PA", "42");
            stateFipsMap.put("Rhode Island", "44");
            stateFipsMap.put("RI", "44");
            stateFipsMap.put("South Carolina", "45");
            stateFipsMap.put("SC", "45");
            stateFipsMap.put("South Dakota", "46");
            stateFipsMap.put("SD", "46");
            stateFipsMap.put("Tennessee", "47");
            stateFipsMap.put("TN", "47");
            stateFipsMap.put("Texas", "48");
            stateFipsMap.put("TX", "48");
            stateFipsMap.put("Utah", "49");
            stateFipsMap.put("UT", "49");
            stateFipsMap.put("Vermont", "50");
            stateFipsMap.put("VT", "50");
            stateFipsMap.put("Virginia", "51");
            stateFipsMap.put("VA", "51");
            stateFipsMap.put("Washington", "53");
            stateFipsMap.put("WA", "53");
            stateFipsMap.put("West Virginia", "54");
            stateFipsMap.put("WV", "54");
            stateFipsMap.put("Wisconsin", "55");
            stateFipsMap.put("WI", "55");
            stateFipsMap.put("Wyoming", "56");
            stateFipsMap.put("WY", "56");
            stateFipsMap.put("District of Columbia", "11");
            stateFipsMap.put("DC", "11");
            stateFipsMap.put("American Samoa", "60");
            stateFipsMap.put("AS", "60");
            stateFipsMap.put("Guam", "66");
            stateFipsMap.put("GU", "66");
            stateFipsMap.put("Northern Mariana Islands", "69");
            stateFipsMap.put("MP", "69");
            stateFipsMap.put("Puerto Rico", "72");
            stateFipsMap.put("PR", "72");
            stateFipsMap.put("U.S. Virgin Islands", "78");
            stateFipsMap.put("VI", "78");
        }

        /**
         * Returns the FIPS code for the given state name or abbreviation.
         *
         * @param stateNameOrAbbreviation The name or abbreviation of the state.
         * @return The FIPS code for the state, or null if the state name or abbreviation is not found.
         */
        public static String getStateFipsCode(String stateNameOrAbbreviation) {
            return stateFipsMap.get(stateNameOrAbbreviation);
        }

}
