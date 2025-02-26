package com.example.msi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsiApplication.class, args);
	}
}
