package com.swam.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.swam.commons.MessageHandler;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.gateway" })
public class GatewayApplication {

	@SuppressWarnings("unused")
	private final MessageHandler requestHandler;

	public GatewayApplication(MessageHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
