package com.timeseries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.timeseries" })
public class TimeSeriesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeSeriesApiApplication.class, args);
	}
}
