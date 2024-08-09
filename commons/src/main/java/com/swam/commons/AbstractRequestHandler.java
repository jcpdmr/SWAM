package com.swam.commons;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.MessageProperties;

import com.swam.commons.OrchestratorInfo.TargetMethods;

public abstract class AbstractRequestHandler {

    private Map<TargetMethods, MethodExecutor> methodsMap;
    private final RabbitMQSender rabbitMQSender;

    public AbstractRequestHandler(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
        this.methodsMap = new HashMap<>();
        methodsMap.put(TargetMethods.NULL, null);
    }

    public void addMethod(TargetMethods targetMethod, MethodExecutor methodExecutor) {
        methodsMap.put(targetMethod, methodExecutor);
    }

    protected abstract void listener(CustomMessage request, MessageProperties messageProperties);

    protected void handle(CustomMessage request, MessageProperties messageProperties) {

        System.out.println("Messaggio ricevuto dall'handler: " + request.getMsg());
        // System.out.println("Messagge properties: " + messageProperties);

        OrchestratorInfo orchestratorInfo = new OrchestratorInfo(messageProperties);
        TargetMethods method = orchestratorInfo.getTargetMethod();
        // System.out.println("Lista di bindings dei metodi: " + methodsMap);
        // System.out.println("Nome del motodo da eseguire: " + method);

        Boolean requestHandledCorrectly = true;

        if (method.equals(TargetMethods.NULL)) {
            System.out.println("execute NULL method");
        } else {
            MethodExecutor methodExecutor = methodsMap.get(method);
            if (methodExecutor != null) {
                methodExecutor.execute();
            } else {
                System.out.println("Method: [" + method + "] not binded, current bindings: " + methodsMap);

                requestHandledCorrectly = false;
            }
        }

        if (requestHandledCorrectly) {
            rabbitMQSender.sendToNextHop(request, false, orchestratorInfo);
        } else {
            // TODO: notify gateway (BAD REQUEST)
        }

    }

    @FunctionalInterface
    public interface MethodExecutor {
        public void execute();
    }
}
