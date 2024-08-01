package com.swam.catalog;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@RabbitListener(queues = "catalog_in")
	public void requestHandler(String msg) {
		System.out.println("Messaggio ricevuto dall'handler: " + msg);
	}
}
