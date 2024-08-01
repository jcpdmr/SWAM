package com.swam.catalog;

import java.util.Date;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@GetMapping(value = "/send")
	public ResponseEntity<String> producer() {
		System.out.println("Sending msg" + new Date().toString());

		String msgString1 = "test msg1: " + new Date().toString();
		String msgString2 = "test msg2: " + new Date().toString();

		rabbitTemplate.convertAndSend("swam.gateway", "operation", msgString1);
		rabbitTemplate.convertAndSend("swam.microservices", "operation", msgString2);

		return ResponseEntity.ok("messaggi inviati");
	}
}
