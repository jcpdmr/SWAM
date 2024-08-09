package com.swam.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.operation" })
public class OperationApplication {

    @SuppressWarnings("unused")
    private final MessageHandler requestHandler;

    public OperationApplication(MessageHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(OperationApplication.class, args);
    }

}
