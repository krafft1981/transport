package com.rental.transport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.config.EnableStateMachine;

@SpringBootApplication
@EnableStateMachine
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
