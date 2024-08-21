package com.swam.gateway;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;
import com.swam.commons.RoutingInstructions.TargetMessageHandler;

import lombok.AllArgsConstructor;

import com.swam.commons.CustomMessage.MessageType;
import com.swam.commons.MessageDispatcher.MessageHandler;

@AllArgsConstructor
@Service
public class MicroserviceResponseHandler implements MessageHandler {

    private final AsyncResponseHandler asyncResponseHandler;

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) {
        System.out.println("Execute HandleACK");

        // TODO: monitor response progress with ackHop
        if (context.getMessageType().equals(MessageType.END_MESSAGE)) {
            Integer ackHop = context.getRoutingInstructions().getHopCounter();
            System.out.println("Recived END_MESSAGE from: " + context.getSender());
            System.out.println("Request completed");

            asyncResponseHandler.setDeferredResult(context.getDeferredResultId(), context.getResponseEntity());

        } else if (context.getMessageType().equals(MessageType.ACK)) {
            Integer ackHop = context.getAckHop().get();
            System.out.println("Recived ACK from: " + context.getSender());

        } else if (context.getMessageType().equals(MessageType.ERROR)) {
            System.out.println("Error message: " + context);
            asyncResponseHandler.setDeferredResult(context.getDeferredResultId(), context.getResponseEntity());
            context.setMessageType(MessageType.END_MESSAGE);
        }

    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.CHECK_ACK);
    }

}