package com.example.FruitTrees.Location;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public class LatitudeLongitudeUtilities {
    @Value("${latitude-longititude.decimal.places}")
    public static   int  maxDecimalPlaces;
    private LatitudeLongitudeUtilities() {
    }

    public static String  limitDecimals(String l){
        Double doubleL=Double.valueOf(l);
        BigDecimal bigDecimal= new BigDecimal(doubleL);
        bigDecimal.setScale(maxDecimalPlaces, BigDecimal.ROUND_HALF_UP);
        return  bigDecimal.toString();
    }
}
