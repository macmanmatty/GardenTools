package com.example.FruitTrees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
@EnableCaching
@EnableAsync
public class FruitTreesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FruitTreesApplication.class, args);
	}

}
