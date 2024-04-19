package com.scraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.scraper.jobs"})
public class SpringMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMainApplication.class, args);
	}

}
