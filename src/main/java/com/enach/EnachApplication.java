package com.enach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EnachApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnachApplication.class, args);
	}

}
