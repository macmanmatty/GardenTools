package com.example.FruitTrees.WeatherConroller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request Error")
    public class BadRequestException extends RuntimeException {
        // Constructor
        public BadRequestException(String message) {
            super(message);
        }
    }
