package com.swam.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.swam.commons.intercommunication.MessageDispatcher;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons.intercommunication", "com.swam.commons.task", "com.swam.gateway" })
public class GatewayApplication {

	@SuppressWarnings("unused")
	private final MessageDispatcher requestHandler;

	public GatewayApplication(MessageDispatcher requestHandler) {
		this.requestHandler = requestHandler;
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
