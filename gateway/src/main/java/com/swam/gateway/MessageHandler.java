package com.swam.gateway;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

import com.swam.commons.AbstractMessageHandler;
import com.swam.commons.CustomMessage;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.OrchestratorInfo.TargetMethods;

@Service
public class MessageHandler extends AbstractMessageHandler {

    private final Orchestrator orchestrator;

    public MessageHandler(RabbitMQSender rabbitMQSender, Orchestrator orchestrator) {
        super(rabbitMQSender);
        this.orchestrator = orchestrator;

        this.addMethod(TargetMethods.CHECK_ACK, new HandleACK());

    }

    @Override
    @RabbitListener(queues = "gateway_in")
    protected void listener(CustomMessage message, MessageProperties messageProperties) {
        this.handle(message, messageProperties);
    }

    private class HandleACK implements MethodExecutor {

        // TODO: handle ack (using orchestratorInfo's uuid) and notify clients
        @Override
        public void execute(CustomMessage context) {
            // TODO: implement method
            System.out.println("Execute HandleACK");
            if (context.getAckHop().isPresent()) {

                System.out.println("Ricevuto ACK con hop: " + context.getAckHop().get());
            } else {
                // TODO: handle errors
                System.out.println("Errore ack");
            }

        }

    }
}