package com.swam.operation;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@SpringBootApplication
public class OperationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationApplication.class, args);
	}

	@RabbitListener(queues = "foo-queue")
	public void receiveMessage(String message) {
		System.out.println("Received: " + message);
	}
}
