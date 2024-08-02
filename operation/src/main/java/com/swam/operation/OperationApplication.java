package com.swam.operation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.multimodule" })
public class OperationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperationApplication.class, args);
    }

    @RabbitListener(queues = "operation_in")
    public void requestHandler(String msg) {
        System.out.println("Messaggio ricevuto dall'handler: " + msg);
    }

}
