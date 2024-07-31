package com.swam.operation;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Consumer;


@SpringBootApplication
public class OperationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationApplication.class, args);
	}

	@Bean
    public Consumer<Message<String>> consumerA(){
        return msg ->  {
            System.out.println("Message recived by A: " + msg.getPayload());
        };
    }

    @Bean
    public Consumer<Message<String>> consumerB(){
        return msg ->  {
            System.out.println("Message recived by B: " + msg.getPayload());
        };
    }
}
