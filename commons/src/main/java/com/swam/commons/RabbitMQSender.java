package com.swam.commons;

import java.util.Optional;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToNextHop(Object message, OrchestratorInfo orchestratorInfo, Boolean isFromGateway) {

        if (!isFromGateway) {
            orchestratorInfo.increaseHop();
        }

        TargetMicroservices nextMicroservice = orchestratorInfo.getTargetMicroservice();

        CustomMessagePostProcessor messagePostProcessor = new CustomMessagePostProcessor(orchestratorInfo);

        OrchestratorInfo ackOrchestratorInfo = OrchestratorInfoBuilder.newBuild()
                .withUUID(orchestratorInfo.getUuid())
                .addTargets(TargetMicroservices.GATEWAY, TargetMethods.CHECK_ACK)
                .build();

        CustomMessagePostProcessor ackPostProcessor = new CustomMessagePostProcessor(ackOrchestratorInfo);

        CustomMessage ackMessage = new CustomMessage("ACK");
        ackMessage.setAckHop(Optional.of(orchestratorInfo.getHopCounter()));

        switch (nextMicroservice) {
            case TargetMicroservices.CATALOG:
                sendToCatalog(message, "swam.microservices", messagePostProcessor);
                if (!isFromGateway) {
                    sendToGateway(ackMessage, ackPostProcessor);
                }
                break;
            case TargetMicroservices.OPERATION:
                sendToOperation(message, "swam.microservices", messagePostProcessor);
                if (!isFromGateway) {
                    sendToGateway(ackMessage, ackPostProcessor);
                }
                break;
            case TargetMicroservices.ANALYSIS:
                sendToAnalysis(message, "swam.microservices", messagePostProcessor);
                if (!isFromGateway) {
                    sendToGateway(ackMessage, ackPostProcessor);
                }
                break;
            case TargetMicroservices.GATEWAY:
                sendToGateway(message, messagePostProcessor);
                break;
            case TargetMicroservices.END:
                break;
            default:
                // TODO: handle errors
                break;
        }

    }

    private class CustomMessagePostProcessor implements MessagePostProcessor {

        private final OrchestratorInfo orchestratorInfo;

        public CustomMessagePostProcessor(OrchestratorInfo orchestratorInfo) {
            this.orchestratorInfo = orchestratorInfo;
        }

        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            orchestratorInfo.addHeadersToMessageProperties(message.getMessageProperties());
            return message;
        }

    }

    private void sendToCatalog(Object message, String exchange, MessagePostProcessor messagePostProcessor) {
        rabbitTemplate.convertAndSend(exchange, "catalog", message, messagePostProcessor);
    }

    private void sendToAnalysis(Object message, String exchange, MessagePostProcessor messagePostProcessor) {
        rabbitTemplate.convertAndSend(exchange, "analysis", message, messagePostProcessor);
    }

    private void sendToOperation(Object message, String exchange, MessagePostProcessor messagePostProcessor) {
        rabbitTemplate.convertAndSend(exchange, "operation", message, messagePostProcessor);
    }

    private void sendToGateway(Object message, MessagePostProcessor messagePostProcessor) {
        rabbitTemplate.convertAndSend("swam.gateway", "gateway", message, messagePostProcessor);
    }

}