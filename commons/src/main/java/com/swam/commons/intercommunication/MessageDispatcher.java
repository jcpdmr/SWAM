package com.swam.commons.intercommunication;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;

@Service
public class MessageDispatcher {
    private final Map<List<TargetMessageHandler>, MessageHandler> messageHandlerMap;
    private final RabbitMQSender rabbitMQSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public MessageDispatcher(RabbitMQSender rabbitMQSender, List<MessageHandler> messageHandlers) {
        this.rabbitMQSender = rabbitMQSender;
        this.messageHandlerMap = messageHandlers.stream()
                .collect(Collectors.toMap(MessageHandler::getBinding, Function.identity()));
        messageHandlerMap.put(List.of(TargetMessageHandler.NULL), null);
    }

    @RabbitListener(queues = "${spring.rabbitmq.in-queue}")
    protected void listener(CustomMessage message) {
        try {
            this.dispatchMessage(message);
        } catch (ProcessingMessageException e) {
            logger.error("ProcessingMessageException: " + e.getMessage());
            message.setError(e.getResponseError(), e.getHttpStatusCode(),
                    "ProcessingMessageException: " + e.getMessage());
            rabbitMQSender.sendToNextHop(message, false);
        } catch (RuntimeException e) {
            logger.error("RunTimeException: " + e.getMessage(), e);
            message.setError("Internal server Error", 500,
                    "ProcessingMessageException: " + e.getMessage());
            rabbitMQSender.sendToNextHop(message, false);
        }

    }

    protected void dispatchMessage(CustomMessage message) throws ProcessingMessageException {

        // System.out.println("Messaggio ricevuto dall'handler: " + message.getMsg());

        RoutingInstructions routingInstructions = message.getRoutingInstructions();
        // System.out.println(message.getroutingInstructions());

        TargetMessageHandler targetMessageHandler = routingInstructions.getTargetMethod();

        // System.out.println("Lista di bindings dei metodi: " + methodsMap);
        // System.out.println("Nome del motodo da eseguire: " + method);

        if (targetMessageHandler.equals(TargetMessageHandler.NULL)) {
            logger.info("Execute NULL method");
        } else {

            MessageHandler messageHandler = null;

            for (Entry<List<TargetMessageHandler>, MessageHandler> bindingsEntry : messageHandlerMap.entrySet()) {
                for (TargetMessageHandler binding : bindingsEntry.getKey()) {
                    if (binding.equals(targetMessageHandler)) {
                        messageHandler = bindingsEntry.getValue();
                        break;
                    }
                }
                if (messageHandler != null) {
                    break;
                }
            }

            if (messageHandler != null) {
                logger.info("Execute handler: " + targetMessageHandler);
                // System.out.println("Execute handler: " + targetMessageHandler);
                messageHandler.handle(message, targetMessageHandler);
            } else {

                throw new ProcessingMessageException(
                        "Method: [" + targetMessageHandler + "] not binded, current bindings: " + messageHandlerMap
                                + " . Have you added @Service to Method?",
                        "Internal server error", 500);
            }
        }

        rabbitMQSender.sendToNextHop(message, false);

    }
}
