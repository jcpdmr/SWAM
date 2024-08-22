package com.swam.commons.intercommunication;

import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swam.commons.intercommunication.CustomMessage.MessageType;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMicroservices;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendToNextHop(CustomMessage message, Boolean isFromGateway) {

        RoutingInstructions routingInstructions = message.getRoutingInstructions();
        // Find current sender
        TargetMicroservices sender;
        if (isFromGateway) {
            sender = TargetMicroservices.GATEWAY;
        } else {
            sender = routingInstructions.getTargetMicroservice();
        }

        if (message.getMessageType().equals(MessageType.ERROR)) {
            // If ERROR, send it directly to gateway
            sendToGateway(generateErrorMessage(message, sender));
            return;
        } else if (message.getMessageType().equals(MessageType.END_MESSAGE)) {
            // If END_MESSAGE, do nothing(it means that it has already completed the hop
            // sequence and it's been already received by gateway)
            return;
        }

        // Update hop and sender
        if (!isFromGateway) {
            message.setSender(sender);
            routingInstructions.increaseHop();
        }

        TargetMicroservices nextMicroservice = routingInstructions.getTargetMicroservice();

        switch (nextMicroservice) {
            case TargetMicroservices.CATALOG:
                sendToCatalog(message, "swam.microservices");
                if (!isFromGateway) {
                    sendToGateway(generateAckMessage(message, sender));
                }
                break;
            case TargetMicroservices.OPERATION:
                sendToOperation(message, "swam.microservices");
                if (!isFromGateway) {
                    sendToGateway(generateAckMessage(message, sender));
                }
                break;
            case TargetMicroservices.ANALYSIS:
                sendToAnalysis(message, "swam.microservices");
                if (!isFromGateway) {
                    sendToGateway(generateAckMessage(message, sender));
                }
                break;
            case TargetMicroservices.GATEWAY:
                // set type to flag the correct termination of hop sequence (last hop to
                // gateway)
                message.setMessageType(MessageType.END_MESSAGE);
                sendToGateway(message);
                break;
            case TargetMicroservices.END:
                // TODO: handle missed return to gateway in the hop sequence errors
                break;
            default:
                // TODO: handle errors
                break;
        }

    }

    private CustomMessage generateAckMessage(CustomMessage originalMessage, TargetMicroservices sender) {

        RoutingInstructions ackRoutingInstructions = RoutingInstructionsBuilder.newBuild()
                .addTargets(TargetMicroservices.GATEWAY, TargetMessageHandler.CHECK_ACK)
                .build();

        CustomMessage ackMessage = new CustomMessage("ACK", ackRoutingInstructions, sender, MessageType.ACK,
                ResponseEntity.ok(null));

        ackMessage.setAckHop(Optional.of(originalMessage.getRoutingInstructions().getHopCounter()));
        ackMessage.setDeferredResultId(originalMessage.getDeferredResultId());
        return ackMessage;
    }

    private CustomMessage generateErrorMessage(CustomMessage originalMessage, TargetMicroservices sender) {
        RoutingInstructions errorRoutingInstructions = RoutingInstructionsBuilder.newBuild()
                .addTargets(TargetMicroservices.GATEWAY, TargetMessageHandler.CHECK_ACK)
                .build();

        CustomMessage errorMessage = new CustomMessage("ERROR", errorRoutingInstructions,
                sender,
                MessageType.ERROR,
                originalMessage.getResponseEntity());

        errorMessage.setDeferredResultId(originalMessage.getDeferredResultId());
        return errorMessage;
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