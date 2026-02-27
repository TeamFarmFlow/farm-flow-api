package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJpaAuditing
@SpringBootApplication
public class FarmFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmFlowApplication.class, args);
	}
}
