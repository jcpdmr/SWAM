package com.swam.gateway;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swam.multimodule.commons.RabbitMQSenderGateway;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.multimodule", "com.swam.gateway" })
@RestController
public class GatewayApplication {

	@Autowired
	private final RabbitMQSenderGateway rabbitMQSender;

	public GatewayApplication(RabbitMQSenderGateway rabbitMQSender) {
		this.rabbitMQSender = rabbitMQSender;
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@GetMapping(value = "/send")
	public ResponseEntity<String> producer() {
		System.out.println("Sending msg" + new Date().toString());

		String msgToCatalog = "hello catalog from gateway: " + new Date().toString();
		String msgToOperation = "hello operation from gateway: " + new Date().toString();
		String msgToAnalysis = "hello analysis from gateway: " + new Date().toString();

		rabbitMQSender.sendToCatalog(msgToCatalog);
		rabbitMQSender.sendToAnalysis(msgToAnalysis);
		rabbitMQSender.sendToOperation(msgToOperation);

		return ResponseEntity.ok("msg sended");
	}
}
