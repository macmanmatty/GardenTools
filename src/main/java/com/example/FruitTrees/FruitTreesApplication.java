package com.example.FruitTrees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class FruitTreesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FruitTreesApplication.class, args);
	}

}
