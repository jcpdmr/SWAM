package com.swam.gateway;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

import com.swam.commons.AbstractMessageHandler;
import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.CustomMessage.MessageType;
import com.swam.commons.OrchestratorInfo.TargetMethods;

@Service
public class MessageHandler extends AbstractMessageHandler {

    private final Dispatcher dispatcher;

    public MessageHandler(RabbitMQSender rabbitMQSender, Dispatcher dispatcher) {
        super(rabbitMQSender);
        this.dispatcher = dispatcher;

        this.addMethod(TargetMethods.CHECK_ACK, new HandleACK());

    }

    @Override
    @RabbitListener(queues = "gateway_in")
    protected void listener(CustomMessage message) {
        this.handle(message);
    }

    private class HandleACK implements MethodExecutor {

        @Override
        public void execute(CustomMessage context) {
            System.out.println("Execute HandleACK");

            UUID orchestrationUUID = context.getOrchestratorInfo().getUuid();

            if (dispatcher.getActiveOrchestratorInfo(orchestrationUUID).isPresent()) {
                OrchestratorInfo orchestratorInfo = dispatcher.getActiveOrchestratorInfo(orchestrationUUID).get();

                if (context.getMessageType().equals(MessageType.END_MESSAGE)) {
                    Integer lastHop = context.getOrchestratorInfo().getHopCounter();

                    System.out.println("Recived END_MESSAGE from: " + context.getSender());
                    System.out.println("Progress: [" + lastHop + "/" + (orchestratorInfo.getMaxHop() - 1) + "]");
                    System.out.println("Request completed");
                    dispatcher.removeActiveOrchestration(orchestrationUUID);

                    // TODO: handle response and notification to client
                } else if (context.getMessageType().equals(MessageType.ACK)) {
                    Integer ackHop = context.getAckHop().get();

                    System.out.println("Recived ACK from: " + context.getSender());
                    System.out.println("Progress: [" + ackHop + "/" + (orchestratorInfo.getMaxHop() - 1) + "]");
                }
            } else {
                System.out.println("No active orchestration with uuid: " + orchestrationUUID);
                // TODO: handle error
            }

        }

    }
}