package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ConfigurationPropertiesScan("com.example.demo.config")
@SpringBootApplication
public class SareetaApplication {
	public static void main(String[] args) {
		SpringApplication.run(SareetaApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return  new BCryptPasswordEncoder();
	}

}
