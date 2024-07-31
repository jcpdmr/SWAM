package com.swam.catalog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@Controller
public class CatalogApplication {

	public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

    // @Autowired
	// private StreamBridge streamBridge;

	@RequestMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<String> producer() {
		System.out.println("Sending ");
		// Message<String> msgA = MessageBuilder.withPayload("test msgA " + new Date().toString()).setHeader("myroutekey", "consumerA").setHeader("contentType", "text/plain").build();
		// Message<String> msgB = MessageBuilder.withPayload("test msgB " + new Date().toString()).setHeader("myroutekey", "consumerB").setHeader("contentType", "text/plain").build();
		// Message<String> msgC = MessageBuilder.withPayload("test msgC " + new Date().toString()).setHeader("myroutekey", "consumerC").setHeader("contentType", "text/plain").build();
		// Message<String> msgD = MessageBuilder.withPayload("test msgD " + new Date().toString()).setHeader("myroutekey", "consumerD").setHeader("contentType", "text/plain").build();
		// streamBridge.send("operation_in", msgA);
		// streamBridge.send("operation_in", msgB);
		// streamBridge.send("analysis_in", msgC);
		// streamBridge.send("analysis_in", msgD);
        return ResponseEntity.ok("messaggi inviati");
	}
}


