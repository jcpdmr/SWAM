package com.swam.gateway;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.CustomMessage.MessageType;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;

@Service
public class MicroserviceResponseHandler extends MessageHandler {

    private final AsyncResponseHandler asyncResponseHandler;

    public MicroserviceResponseHandler(AsyncResponseHandler asyncResponseHandler) {
        super(List.of(TargetMessageHandler.CHECK_ACK));
        this.asyncResponseHandler = asyncResponseHandler;
    }

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) {

        // TODO: monitor response progress with ackHop
        if (context.getMessageType().equals(MessageType.END_MESSAGE)) {
            Integer ackHop = context.getRoutingInstructions().getHopCounter();
            logger.info("Recived END_MESSAGE from: " + context.getSender());
            logger.info("Request completed");

            // Notify Client setting DeferredResult
            asyncResponseHandler.setDeferredResult(context.getDeferredResultId(),
                    context.getResponseEntity());

        } else if (context.getMessageType().equals(MessageType.ACK)) {
            Integer ackHop = context.getAckHop().get();
            logger.info("Recived ACK from: " + context.getSender());

        } else if (context.getMessageType().equals(MessageType.ERROR)) {
            logger.warn("Error message: " + context);
            asyncResponseHandler.setDeferredResult(context.getDeferredResultId(), context.getResponseEntity());
            context.setMessageType(MessageType.END_MESSAGE);
        }

    }

}