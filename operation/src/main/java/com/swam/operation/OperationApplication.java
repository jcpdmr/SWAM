package com.swam.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.swam.commons.intercommunication.MessageDispatcher;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.swam.commons.mongodb.istance")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.operation" })
public class OperationApplication {

    @SuppressWarnings("unused")
    private final MessageDispatcher requestHandler;

    public OperationApplication(MessageDispatcher requestHandler) {
        this.requestHandler = requestHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(OperationApplication.class, args);
    }

}
