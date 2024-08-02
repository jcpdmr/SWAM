package com.swam.multimodule.commons;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSenderMicroservices {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSenderMicroservices(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToCatalog(String message) {
        rabbitTemplate.convertAndSend("swam.microsevices", "catalog", message);
    }

    public void sendToAnalysis(String message) {
        rabbitTemplate.convertAndSend("swam.microsevices", "analysis", message);
    }

    public void sendToOperation(String message) {
        rabbitTemplate.convertAndSend("swam.microsevices", "operation", message);
    }
}