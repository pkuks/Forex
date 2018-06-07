package com.singtel.Forex;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ForexApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ForexApplication.class, args);
	}
	
	@PostConstruct
	void setUTCTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
