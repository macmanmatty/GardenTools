package com.example.FruitTrees.ChillingHours.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValues;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  A weather processor that calculates the total amount of
 *  counting chilling hours (temperature ) for deciduous fruit trees
 * using the Utah Method
 * from 11/1 to 3/31
 */
@Component("UtahChillDynamic")
public class DynamicUtahChillCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double chillHours;
    /**
     * the min value
     */
    private List<Double> ragesBottom= new ArrayList<>();
    private List<Double> ragesTop= new ArrayList<>();

    /**
     * the max value
     */
    private List<Double> chillHoursValues=new ArrayList<>();
    public DynamicUtahChillCalculator() {
        super("Chill Hours");
    }
    @Override
    public void before() {
        if(inputParameters.size()%3!=0){
            throw new IllegalArgumentException("Incorrect Number of Parameters");
        }
        int size=values.size();
        for(int count=0; count<size; count=count+3){
            ragesBottom.add(Double.valueOf(inputParameters.get(count)));
            ragesTop.add(Double.valueOf(inputParameters.get(count+1)));
            chillHoursValues.add(Double.valueOf(inputParameters.get(count+2)));
        }

        values.clear();
    }
    @Override
    protected void onStartDate(String date) {
    }
    @Override
    protected void onEndDate(String date) {
        LocalDateTime localDateTime=LocalDateTime.parse(date);
        int year= localDateTime.getYear();
       YearlyValues yearlyValues= locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String years= text+ " Utah Calculation Method ";
        yearlyValues.getValues().put(years, String.valueOf(chillHours));
        values.add(years+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ chillHours);
        chillHours =0;
    }
    @Override
    void processWeatherBetween(Number data, String date) {
        double value=data.doubleValue();

    }
}
