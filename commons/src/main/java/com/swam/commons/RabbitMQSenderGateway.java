package com.swam.commons;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSenderGateway {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSenderGateway(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToCatalog(Object message, Boolean toBeConverted) {
        if (toBeConverted) {
            rabbitTemplate.convertAndSend("swam.gateway", "catalog", message);
        } else {
            rabbitTemplate.send("swam.gateway", "catalog", (Message) message);
        }
    }

    public void sendToAnalysis(Object message) {
        rabbitTemplate.convertAndSend("swam.gateway", "analysis", message);
    }

    public void sendToOperation(Object message) {
        rabbitTemplate.convertAndSend("swam.gateway", "operation", message);
    }
}