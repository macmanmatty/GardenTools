package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *  A weather processor that calculates the total amount of
 *  counting chilling hours (temperature ) for deciduous fruit trees
 * using the Utah Method
 * from 11/1 to 3/31
 */
@Component("UtahChill")
@Scope("prototype")

public class UtahChillCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double chillHours;
    public UtahChillCalculator() {
        super("  Utah Chill Hours");
    }
    @Override
    protected void onEndDate(String date) {
        int year= DateUtilities.getYear(date);
        super.yearlyDataValues.add(chillHours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String years= text+ " Utah Calculation Method ";
        yearlyValuesResponse.getValues().put(years, String.valueOf(chillHours));
        addProcessedTextValue(years+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ chillHours);
        chillHours =0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();
        if( value>=34 && value<=45) {
            chillHours++;
        }
          else  if( value>=45 && value<=55) {
            chillHours=chillHours+.5;
        }
          else if(value>55 && value<=65){
              chillHours=chillHours-.5;
        }
        else if(value>65){
            chillHours--;
        }
    }
}
