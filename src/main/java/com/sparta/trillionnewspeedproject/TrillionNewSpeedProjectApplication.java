package com.sparta.trillionnewspeedproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrillionNewSpeedProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrillionNewSpeedProjectApplication.class, args);
	}

}
