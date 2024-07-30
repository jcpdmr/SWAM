package com.swam.operation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @RabbitListener(queues = "foo-queue")
    public void receiveMessage(String message) {
        System.out.println("Received: " + message);
    }
}
