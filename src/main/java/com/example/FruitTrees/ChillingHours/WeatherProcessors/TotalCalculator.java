package com.example.FruitTrees.ChillingHours.WeatherProcessors;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component("Total")
public class TotalCalculator extends WeatherProcessor {
    private double total;
    private double maxTemp;
    private double minTemp;
    private boolean inDate;

    private boolean counting;

    public TotalCalculator() {
        super("Total");
    }

    @Override
    public void before() {

    }

    @Override
    public void processWeather(Number  number, String date) {
        double value=number.doubleValue();
        if (inDate) {
            this.total = this.total + value;
            counting=true;
        }
            if( counting && isEndDate(date)){
                LocalDateTime localDateTime=LocalDateTime.parse(date);
                addValue(total, localDateTime.getYear() );
                total =0;

        }
    }

}
