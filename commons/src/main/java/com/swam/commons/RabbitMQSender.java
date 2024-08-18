package com.swam.commons;

import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage.MessageType;
import com.swam.commons.OrchestratorInfo.TargetTasks;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToNextHop(CustomMessage message, Boolean isFromGateway) {

        OrchestratorInfo orchestratorInfo = message.getOrchestratorInfo();

        TargetMicroservices sender = null;
        OrchestratorInfo ackOrchestratorInfo = null;
        CustomMessage ackMessage = null;

        if (!isFromGateway) {
            sender = orchestratorInfo.getTargetMicroservice();
            message.setSender(sender);

            orchestratorInfo.increaseHop();

            ackOrchestratorInfo = OrchestratorInfoBuilder.newBuild()
                    .withUUID(orchestratorInfo.getUuid())
                    .addTargets(TargetMicroservices.GATEWAY, TargetTasks.CHECK_ACK)
                    .build();

            ackMessage = new CustomMessage("ACK", ackOrchestratorInfo, sender, MessageType.ACK,
                    ResponseEntity.ok(null));
            ackMessage.setAckHop(Optional.of(orchestratorInfo.getHopCounter()));
        }

        TargetMicroservices nextMicroservice = orchestratorInfo.getTargetMicroservice();

        switch (nextMicroservice) {
            case TargetMicroservices.CATALOG:
                sendToCatalog(message, "swam.microservices");
                if (!isFromGateway) {
                    sendToGateway(ackMessage);
                }
                break;
            case TargetMicroservices.OPERATION:
                sendToOperation(message, "swam.microservices");
                if (!isFromGateway) {
                    sendToGateway(ackMessage);
                }
                break;
            case TargetMicroservices.ANALYSIS:
                sendToAnalysis(message, "swam.microservices");
                if (!isFromGateway) {
                    sendToGateway(ackMessage);
                }
                break;
            case TargetMicroservices.GATEWAY:
                message.setMessageType(MessageType.END_MESSAGE);
                sendToGateway(message);
                break;
            case TargetMicroservices.END:
                break;
            default:
                // TODO: handle errors
                break;
        }

    }

    private void sendToCatalog(CustomMessage message, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "catalog", message);
    }

    private void sendToAnalysis(CustomMessage message, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "analysis", message);
    }

    private void sendToOperation(CustomMessage message, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "operation", message);
    }

    private void sendToGateway(CustomMessage message) {
        rabbitTemplate.convertAndSend("swam.gateway", "gateway", message);
    }

}