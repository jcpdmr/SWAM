package com.swam.commons.intercommunication;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;

@Service
public class MessageDispatcher {
    private final Map<List<TargetMessageHandler>, MessageHandler> messageHandlerMap;
    private final RabbitMQSender rabbitMQSender;

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
            System.err.println("ProcessingMessageException: " + e.getMessage());
            message.setError(e.getResponseError(), e.getHttpStatusCode());
            rabbitMQSender.sendToNextHop(message, false);
        } catch (RuntimeException e) {
            e.printStackTrace();
            message.setError("Internal server Error", 500);
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
            System.out.println("execute NULL method");
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

    public interface MessageHandler {
        public void handle(CustomMessage context, TargetMessageHandler triggeredBinding)
                throws ProcessingMessageException;

        public List<TargetMessageHandler> getBinding();

        public default String getUriId(CustomMessage context, ApiTemplateVariable apiTemplateVariable,
                Boolean isRequired)
                throws ProcessingMessageException {
            Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
            String uriId = uriTemplateVariables.get(apiTemplateVariable.value());
            if (isRequired && uriId == null) {
                throw new ProcessingMessageException(context.getRequestMethod() + " with null " + apiTemplateVariable,
                        "Internal server error", 500);
            }
            return uriId;

        }
    }
}
