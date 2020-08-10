package com.rental.transport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransportApplication {

	public static final String USER_HEADER = "Profile";
	public static void main(String[] args) {
		SpringApplication.run(TransportApplication.class, args);
	}
}
