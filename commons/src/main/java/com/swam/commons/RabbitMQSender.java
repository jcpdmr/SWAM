package com.swam.commons;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.swam.commons.OrchestratorInfo.TargetMicroservices;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToNextHop(Message message, Boolean isGateway) {

        OrchestratorInfo orchestratorInfo = new OrchestratorInfo(message.getMessageProperties());
        if (!isGateway) {
            orchestratorInfo.increaseHop();
        }

        TargetMicroservices nextMicroservice = orchestratorInfo.getTargetMicroservice();
        MessageProperties updatedMsgProperties = orchestratorInfo.toMessageProperties();

        message = MessageBuilder.withBody(message.getBody()).andProperties(updatedMsgProperties)
                .build();
        Message ackMessage = MessageBuilder.withBody("ACK".getBytes()).andProperties(updatedMsgProperties)
                .build();

        switch (nextMicroservice) {
            case TargetMicroservices.CATALOG:
                sendToCatalog(message, "swam.microservices");
                if (!isGateway) {
                    sendToGateway(ackMessage);
                }
                break;
            case TargetMicroservices.OPERATION:
                sendToOperation(message, "swam.microservices");
                if (!isGateway) {
                    sendToGateway(ackMessage);
                }
                break;
            case TargetMicroservices.ANALYSIS:
                sendToAnalysis(message, "swam.microservices");
                if (!isGateway) {
                    sendToGateway(ackMessage);
                }
                break;
            case TargetMicroservices.GATEWAY:
                sendToGateway(message);
                break;
            default:
                // TODO: handle errors
                break;
        }

    }

    private void sendToCatalog(Message message, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "catalog", message);
    }

    private void sendToAnalysis(Message message, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "analysis", message);
    }

    private void sendToOperation(Message message, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "operation", message);
    }

    private void sendToGateway(Message message) {
        rabbitTemplate.convertAndSend("swam.gateway", "gateway", message);
    }

}