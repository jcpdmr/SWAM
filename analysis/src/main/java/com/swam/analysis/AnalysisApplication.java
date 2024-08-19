package com.swam.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.swam.commons.MessageDispatcher;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.analysis" })
public class AnalysisApplication {

	@SuppressWarnings("unused")
	private final MessageDispatcher requestHandler;

	public AnalysisApplication(MessageDispatcher requestHandler) {
		this.requestHandler = requestHandler;
	}

	public static void main(String[] args) {
		SpringApplication.run(AnalysisApplication.class, args);
	}
}
