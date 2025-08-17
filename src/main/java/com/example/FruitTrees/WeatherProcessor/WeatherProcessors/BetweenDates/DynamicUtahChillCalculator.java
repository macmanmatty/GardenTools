package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")

public class DynamicUtahChillCalculator extends ProcessWeatherBetweenDates {
    /**
     * the counted hours
     */
    private double chillHours;
    /**
     * the min value
     */
    private final List<Double> ragesBottom= new ArrayList<>();

    /**
     * the max value
     */
    private final  List<Double> ragesTop= new ArrayList<>();

    private final  List<Double> chillAmounts=new ArrayList<>();
    public DynamicUtahChillCalculator() {
        super("Dynamic Utah Chill Hours");
        ragesBottom.add(34d);
        ragesBottom.add(45d);
        ragesBottom.add(55d);
        ragesBottom.add(65d);
        ragesTop.add(45d);
        ragesTop.add(55d);
        ragesTop.add(65d);
        ragesTop.add(500d);
        chillAmounts.add(.5d);
        chillAmounts.add(1d);
        chillAmounts.add(-.5d);
        chillAmounts.add(-1d);
        
    }
    @Override
    public void before() {
        clearProcessedTextValues();
    }

    @Override
    protected void onEndDate(LocalDateTime date) {
       int year= date.getYear();
        super.yearlyDataValues.add(chillHours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String years= text+ "  Dynamic Utah Calculation Method ";
        yearlyValuesResponse.getValues().put(years, String.valueOf(chillHours));
        addProcessedTextValue(years+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ chillHours);
        chillHours =0;
    }
    @Override
    protected void processWeatherBetween(Number data, LocalDateTime date) {
        double value=data.doubleValue();
        int size=ragesBottom.size();
        for(int count=0; count<size; count++){
            double min=ragesBottom.get(count);
            double max=ragesTop.get(count);
            double chillAmount=chillAmounts.get(count);
            if(value>=min && value<=max){
                chillHours=chillHours+chillAmount;
            }
        }

    }
}
