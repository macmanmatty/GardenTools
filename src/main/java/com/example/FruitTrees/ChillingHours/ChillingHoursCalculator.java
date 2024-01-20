package com.example.FruitTrees.ChillingHours;

import com.example.FruitTrees.OpenMeteo.OpenMeteoResponse;
import com.example.FruitTrees.OpenMeteo.WeatherRequest;
import com.example.FruitTrees.OpenMeteo.WeatherResponse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChillingHoursCalculator {
    public void calculateChillingHours(WeatherResponse chillingHoursResponse , WeatherRequest chillingHoursRequest, OpenMeteoResponse openMeteoResponse){
        Map< String,   Double> chillingHours=new HashMap<>();
        List<ChillingCalculationMethod>  chillingCalculationMethods=chillingHoursRequest.getChillingHoursCalculationMethods();
        for(ChillingCalculationMethod chillingCalculationMethod: chillingCalculationMethods) {
            double maxChillingTemp=chillingCalculationMethod.getMaxChillingTemp();
            double minChillingTemp=chillingCalculationMethod.getMinChillingTemp();
            List<Double> doubles = openMeteoResponse.getHourly().getTemperature2m();
            double chillHours=0;
            for (double temp : doubles) {
                if (temp<=maxChillingTemp && temp>=minChillingTemp){
                    chillHours++;
                }
            }
            chillingHours.put(chillingCalculationMethod.getName(), chillHours);
        }

    }
    public WeatherResponse calculateYearlyWeather(WeatherResponse chillingHoursResponse , WeatherRequest chillingHoursRequest, OpenMeteoResponse openMeteoResponse){
        WeatherResponse hoursResponse=new WeatherResponse();
        List<ChillingCalculationMethod>  chillingCalculationMethods=chillingHoursRequest.getChillingHoursCalculationMethods();
        List<Double>  temps= openMeteoResponse.getHourly().getTemperature2m();
        List<Double>  rain= openMeteoResponse.getHourly().getRain();
        List<Double>  snow= openMeteoResponse.getHourly().getSnowfall();
        LocalDate startDate=LocalDate.parse(chillingHoursRequest.getStartDate());
        LocalDate endDate=LocalDate.parse(chillingHoursRequest.getEndDate());
        List<String> time=openMeteoResponse.getHourly().getTime();
        int size=time.size();
        YearlyWeather yearlyWeather =new YearlyWeather();
        Map<String , Double> countedHours=new HashMap<>();
        boolean countChill=chillingHoursRequest.isCalculateYearlyChill();
        boolean countRain=chillingHoursRequest.isCalculateYearlyRainFall();
        boolean countSnow=chillingHoursRequest.isCalculateYearlySnowFall();
        double maxTemp=-2000;
        double minTemp=2000;
        boolean countingChill = false;
        boolean countingPrecipitation = false;
        double rainFall=0;
        double snowFall=0;
        int chillStartMonth=chillingHoursRequest.getStartChillMonth();
        int chillEndMonth=chillingHoursRequest.getEndChillMonth();
        int chillStartDay=chillingHoursRequest.getStartChillDay();
        int chillEndDay=chillingHoursRequest.getEndChillDay();
        int precipitationStartDay=chillingHoursRequest.getStartPrecipitationDay();
        int precipitationEndDay=chillingHoursRequest.getEndPrecipitationDay();
        int precipitationStartMonth=chillingHoursRequest.getStartPrecipitationMonth();
        int precipitationEndMonth=chillingHoursRequest.getEndPrecipitationMonth();
        String years=""+startDate.getYear()+"-"+endDate.getYear();
        yearlyWeather.setYear(years);
        for(int count=0; count<size ; count++){
            LocalDate currentDate=LocalDate.parse(getDate(time.get(count)));
            if(currentDate.getMonthValue()==chillStartMonth && currentDate.getDayOfMonth()==chillStartDay){
               countingChill=true;
            }
           if(currentDate.getMonthValue()==chillEndMonth && currentDate.getDayOfMonth()==chillEndDay){
                countingChill=false;
                yearlyWeather.getValues().putAll(countedHours);
           }
            if(currentDate.getMonthValue()==precipitationStartMonth && currentDate.getDayOfMonth()==precipitationStartDay){
                countingPrecipitation=true;
            }
            if(currentDate.getMonthValue()==precipitationEndMonth && currentDate.getDayOfMonth()==precipitationEndDay){
                countingPrecipitation=false;
                yearlyWeather.getValues().put("Total Rain Fall: ", rainFall);
                yearlyWeather.getValues().put("Total Rain  SnowFall: ", snowFall);
                yearlyWeather.getValues().put("Total Precipitation: ", snowFall+rainFall);
                yearlyWeather.getValues().put("Max Temp", maxTemp);
                yearlyWeather.getValues().put("Min Temp", minTemp);

            }
            double temp=temps.get(count);
           if(temp>maxTemp){
               maxTemp=temp;
           }
            if(temp<minTemp){
                minTemp=temp;
            }
            if(countingChill && countChill){
                for(ChillingCalculationMethod chillingCalculationMethod:chillingCalculationMethods ){
                    double maxChillingTemp=chillingCalculationMethod.getMaxChillingTemp();
                    double minChillingTemp=chillingCalculationMethod.getMinChillingTemp();
                    String name=chillingCalculationMethod.getName();
                    if(countedHours.get(name)==null){
                        countedHours.put(name, 0d);
                    }
                    else if (temp<=maxChillingTemp && temp>=minChillingTemp){
                        double hours=countedHours.get(name)+1;
                        countedHours.put(name, hours);
                    }
                }
            }
            if(countingPrecipitation && countRain){
                rainFall=rainFall+rain.get(count);

            }
            if(countingPrecipitation && countSnow){
                snowFall=snowFall+snow.get(count);
            }

        }
        return chillingHoursResponse;
    }


    public String getDate(String dateAndTime){
        String [] dates=dateAndTime.split("T");
        return dates[0];
    }

}
