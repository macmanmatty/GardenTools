package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import com.example.FruitTrees.WeatherProcessor.WeatherProcessors.Bin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  A weather processor that calculates the total amount of
 *  counting chilling hours (temperature ) for deciduous fruit trees
 * using the Utah Method
 * from 11/1 to 3/31
 */
@Component("UtahChill")
@Scope("prototype")

public class UtahChillCalculator extends ProcessWeatherBetweenDates {

    private double chillUnits;

    // immutable bins: [minInclusive, maxExclusive, weight]
    public UtahChillCalculator() {
        super("Dynamic Utah Chill Hours");

    }
    @Override
    public void before() {
        clearProcessedTextValues();
        // if no bins provided with request set  default utah chill calculation bins
        if(bins.isEmpty()){
            bins = List.of(
                    new Bin(Double.NEGATIVE_INFINITY, 34.0,  0.0),   // <34
                    new Bin(34.0,  37.0,  0.5),                     // 35–36
                    new Bin(37.0,  49.0,  1.0),                     // 37–48
                    new Bin(49.0,  55.0,  0.5),                     // 49–54
                    new Bin(55.0,  61.0,  0.0),                     // 55–60
                    new Bin(61.0,  66.0, -0.5),                     // 61–65
                    new Bin(66.0,  Double.POSITIVE_INFINITY, -1.0)  // >65
            );
        }
    }

    @Override
    protected void onEndDate(LocalDateTime date) {
       int year= date.getYear();
        super.yearlyDataValues.add(chillUnits);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String years= text+ "  Dynamic Utah Calculation Method ";
        yearlyValuesResponse.getValues().put(years, String.valueOf(chillUnits));
        addProcessedTextValue(years+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ chillUnits);
        chillUnits =0;
    }


    @Override
    protected void processWeatherBetween(Number data, LocalDateTime date) {
        double tF = data.doubleValue(); // ensure this is °F upstream
        for (Bin b : bins) {
            if (tF >= b.min() && tF < b.max()) {
                chillUnits += b.weight();
                break;
            }
        }
    }


}
