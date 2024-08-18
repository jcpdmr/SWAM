package com.swam.commons;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage.MessageType;
import com.swam.commons.OrchestratorInfo.TargetTasks;

@Service
public class MessageHandler {

    private final Map<TargetTasks, TaskExecutor> methodsMap;
    private final RabbitMQSender rabbitMQSender;

    public MessageHandler(RabbitMQSender rabbitMQSender, List<TaskExecutor> methodExecutors) {
        this.rabbitMQSender = rabbitMQSender;
        this.methodsMap = methodExecutors.stream()
                .collect(Collectors.toMap(TaskExecutor::getBinding, Function.identity()));
        methodsMap.put(TargetTasks.NULL, null);
    }

    @RabbitListener(queues = "${spring.rabbitmq.in-queue}")
    protected void listener(CustomMessage message) {
        this.handle(message);
    }

    protected void handle(CustomMessage message) {

        // System.out.println("Messaggio ricevuto dall'handler: " + message.getMsg());

        OrchestratorInfo orchestratorInfo = message.getOrchestratorInfo();
        // System.out.println(message.getOrchestratorInfo());

        TargetTasks method = orchestratorInfo.getTargetMethod();

        // System.out.println("Lista di bindings dei metodi: " + methodsMap);
        // System.out.println("Nome del motodo da eseguire: " + method);

        Boolean messageHandledCorrectly = true;

        if (method.equals(TargetTasks.NULL)) {
            System.out.println("execute NULL method");
        } else {
            TaskExecutor methodExecutor = methodsMap.get(method);
            if (methodExecutor != null) {
                methodExecutor.execute(message);
            } else {
                System.out.println("Method: [" + method + "] not binded, current bindings: " + methodsMap
                        + " . Have you added @Service to Method?");

                messageHandledCorrectly = false;
            }
        }

        if (messageHandledCorrectly) {
            if (message.getMessageType().equals(MessageType.TO_BE_FORWARDED)) {
                rabbitMQSender.sendToNextHop(message, false);
            }
        } else {
            // TODO: notify gateway (BAD REQUEST)
        }

    }

    protected RabbitMQSender getRabbitMQSender() {
        return rabbitMQSender;
    }

    public interface TaskExecutor {
        public void execute(CustomMessage context);

        public TargetTasks getBinding();
    }
}
