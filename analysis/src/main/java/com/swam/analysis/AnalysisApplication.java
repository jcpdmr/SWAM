package com.swam.analysis;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalysisApplication.class, args);
	}

	@RabbitListener(queues = "analysis_in")
	public void requestHandler(String msg) {
		System.out.println("Messaggio ricevuto dall'handler: " + msg);
	}
}
