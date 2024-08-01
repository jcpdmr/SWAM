package com.swam.gateway;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToCatalog(String message) {
        rabbitTemplate.convertAndSend("swam.gateway", "catalog", message);
    }

    public void sendToAnalysis(String message) {
        rabbitTemplate.convertAndSend("swam.gateway", "analysis", message);
    }

    public void sendToOperation(String message) {
        rabbitTemplate.convertAndSend("swam.gateway", "operation", message);
    }
}