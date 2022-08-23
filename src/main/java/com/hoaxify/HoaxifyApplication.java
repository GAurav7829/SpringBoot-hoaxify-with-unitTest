package com.hoaxify;

import static com.hoaxify.LogLevel.INFO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HoaxifyApplication {
	private static final LogConfig log = new LogConfig("com.hoaxify.HoaxifyApplication");
	
	public static void main(String[] args) {
		log.logInfo(INFO, "logs start...");
		SpringApplication.run(HoaxifyApplication.class, args);
		log.logInfo(INFO, "logs end...");
	}

}
