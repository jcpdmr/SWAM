package com.swam.commons;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.MessageProperties;

import com.swam.commons.OrchestratorInfo.TargetMethods;

public abstract class AbstractMessageHandler {

    private Map<TargetMethods, MethodExecutor> methodsMap;
    private final RabbitMQSender rabbitMQSender;

    public AbstractMessageHandler(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
        this.methodsMap = new HashMap<>();
        methodsMap.put(TargetMethods.NULL, null);
    }

    public void addMethod(TargetMethods targetMethod, MethodExecutor methodExecutor) {
        // TODO: check if targetMethod already present
        methodsMap.put(targetMethod, methodExecutor);
    }

    protected abstract void listener(CustomMessage message, MessageProperties messageProperties);

    protected void handle(CustomMessage message, MessageProperties messageProperties) {

        // System.out.println("Messaggio ricevuto dall'handler: " + message.getMsg());
        // System.out.println("Messagge properties: " + messageProperties);

        OrchestratorInfo orchestratorInfo = new OrchestratorInfo(messageProperties);
        TargetMethods method = orchestratorInfo.getTargetMethod();
        // System.out.println("Lista di bindings dei metodi: " + methodsMap);
        // System.out.println("Nome del motodo da eseguire: " + method);

        Boolean messageHandledCorrectly = true;

        if (method.equals(TargetMethods.NULL)) {
            System.out.println("execute NULL method");
        } else {
            MethodExecutor methodExecutor = methodsMap.get(method);
            if (methodExecutor != null) {
                methodExecutor.execute(message);
            } else {
                System.out.println("Method: [" + method + "] not binded, current bindings: " + methodsMap);

                messageHandledCorrectly = false;
            }
        }

        if (messageHandledCorrectly) {
            rabbitMQSender.sendToNextHop(message, orchestratorInfo, false);
        } else {
            // TODO: notify gateway (BAD REQUEST)
        }

    }

    protected RabbitMQSender getRabbitMQSender() {
        return rabbitMQSender;
    }

    @FunctionalInterface
    public interface MethodExecutor {
        public void execute(CustomMessage context);
    }
}
