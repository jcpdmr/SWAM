package com.swam.commons;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.swam.commons.OrchestratorInfo.TargetMicroservices;

@Service
public class RabbitMQSenderMicroservices {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSenderMicroservices(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToNextHop(Message message) {

        OrchestratorInfo orchestratorInfo = new OrchestratorInfo(message.getMessageProperties());
        TargetMicroservices nextMicroservice = orchestratorInfo.getTargetMicroservice();

        orchestratorInfo.increaseHop();
        message = MessageBuilder.withBody(message.getBody()).andProperties(orchestratorInfo.toMessageProperties())
                .build();

        switch (nextMicroservice) {
            case TargetMicroservices.CATALOG:
                sendToCatalog(message);
                break;
            case TargetMicroservices.OPERATION:
                sendToOperation(message);
                break;
            case TargetMicroservices.ANALYSIS:
                sendToAnalysis(message);
                break;
            default:
                break;
        }
    }

    public void sendToCatalog(Message message) {
        rabbitTemplate.convertAndSend("swam.microservices", "catalog", message);
    }

    public void sendToAnalysis(Message message) {
        rabbitTemplate.convertAndSend("swam.microservices", "analysis", message);
    }

    public void sendToOperation(Message message) {
        rabbitTemplate.convertAndSend("swam.microservices", "operation", message);
    }
}