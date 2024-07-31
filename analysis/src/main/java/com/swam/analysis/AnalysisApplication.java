package com.swam.analysis;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@SpringBootApplication
public class AnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalysisApplication.class, args);
	}

	@Bean
    public Consumer<Message<String>> consumerC(){
        return msg ->  {
            System.out.println("Message recived by C: " + msg.getPayload());
        };
    }

    @Bean
    public Consumer<Message<String>> consumerD(){
        return msg ->  {
            System.out.println("Message recived by D: " + msg.getPayload());
        };
    }

}
