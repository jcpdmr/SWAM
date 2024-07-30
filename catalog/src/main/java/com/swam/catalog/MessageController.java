package com.swam.catalog;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;

@SpringBootApplication
@RestController
public class MessageController {

    // @Autowired
    // private StreamBridge streamBridge;

	public static void main(String[] args) {
		SpringApplication.run(MessageController.class, args);
	}

    @Bean
    public Supplier<String> produceMessage(){
        return () -> {
            return new Date().toString();
        };
    };

    // @GetMapping("/send")
    // public ResponseEntity<String> sendMessage(
    //         @RequestBody String message) {
    //     // Message<String> msg = MessageBuilder.withPayload(message)
    //     //         .setHeader("routingKey", "pippo")
    //     //         .build()

    //     boolean sent = streamBridge.send("testexchange", new Date().toString() + " --> "+ message);
    //     if (sent) {
    //         return ResponseEntity.ok("Messaged sent");
    //     } else {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body("Errore nell'invio del messaggio");
    //     }
    // }

    // @GetMapping("/hello")
    // public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
    //   return String.format("Hello %s!", name);
    // }
}


