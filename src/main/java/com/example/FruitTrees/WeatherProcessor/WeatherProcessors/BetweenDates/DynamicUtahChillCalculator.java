package com.example.FruitTrees.WeatherProcessor.WeatherProcessors.BetweenDates;

import com.example.FruitTrees.Utilities.DateUtilities;
import com.example.FruitTrees.WeatherConroller.WeatherResponse.YearlyValuesResponse;
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
    private final List<Double> ragesBottom= new ArrayList<>();

    /**
     * the max value
     */
    private final  List<Double> ragesTop= new ArrayList<>();

    private final  List<Double> chillAmounts=new ArrayList<>();
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
            chillAmounts.add(Double.valueOf(inputParameters.get(count+2)));
        }
        values.clear();
    }

    @Override
    protected void onEndDate(String date) {
       int year= DateUtilities.getYear(date);
        super.yearlyDataValues.add(chillHours);
        YearlyValuesResponse yearlyValuesResponse = locationWeatherResponse.getYearlyValues(String.valueOf(year));
        String text="Chilling Hours";
        String years= text+ "  Dynamic Utah Calculation Method ";
        yearlyValuesResponse.getValues().put(years, String.valueOf(chillHours));
        addProcessedValue(years+" For " +year+" from: "+ startMonth +"/"+startDay+" to "+endMonth+"/" +endDay+ ": "+ chillHours);
        chillHours =0;
    }
    @Override
    protected void processWeatherBetween(Number data, String date) {
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
