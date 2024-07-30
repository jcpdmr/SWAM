package com.swam.operation;

import org.springframework.context.annotation.Bean;
import java.util.function.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessageConsumer {

    public static void main(String[] args) {
		SpringApplication.run(MessageConsumer.class, args);
	}

    @Bean
    public Consumer<String> consumeMessage() {
        return message -> {
            System.out.println("Payload: " + message);
        };
    }
}
