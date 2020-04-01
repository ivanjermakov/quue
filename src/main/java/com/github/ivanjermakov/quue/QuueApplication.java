package com.github.ivanjermakov.quue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuueApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuueApplication.class, args);
	}
}
