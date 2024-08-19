package com.swam.commons;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.swam.commons.RoutingInstructions.TargetTasks;

@Service
public class MessageDispatcher {

    private final Map<List<TargetTasks>, TaskExecutor> taskMap;
    private final RabbitMQSender rabbitMQSender;

    public MessageDispatcher(RabbitMQSender rabbitMQSender, List<TaskExecutor> methodExecutors) {
        this.rabbitMQSender = rabbitMQSender;
        this.taskMap = methodExecutors.stream()
                .collect(Collectors.toMap(TaskExecutor::getBinding, Function.identity()));
        taskMap.put(List.of(TargetTasks.NULL), null);
    }

    @RabbitListener(queues = "${spring.rabbitmq.in-queue}")
    protected void listener(CustomMessage message) {
        this.dispatchMessage(message);
    }

    protected void dispatchMessage(CustomMessage message) {

        // System.out.println("Messaggio ricevuto dall'handler: " + message.getMsg());

        RoutingInstructions routingInstructions = message.getRoutingInstructions();
        // System.out.println(message.getroutingInstructions());

        TargetTasks targetTask = routingInstructions.getTargetMethod();

        // System.out.println("Lista di bindings dei metodi: " + methodsMap);
        // System.out.println("Nome del motodo da eseguire: " + method);

        if (targetTask.equals(TargetTasks.NULL)) {
            System.out.println("execute NULL method");
        } else {

            TaskExecutor taskExecutor = null;

            for (Entry<List<TargetTasks>, TaskExecutor> bindingsEntry : taskMap.entrySet()) {
                for (TargetTasks binding : bindingsEntry.getKey()) {
                    if (binding.equals(targetTask)) {
                        taskExecutor = bindingsEntry.getValue();
                        break;
                    }
                }
                if (taskExecutor != null) {
                    break;
                }
            }

            if (taskExecutor != null) {
                taskExecutor.execute(message, targetTask);
            } else {
                System.out.println("Method: [" + targetTask + "] not binded, current bindings: " + taskMap
                        + " . Have you added @Service to Method?");

                message.setError("Internal server error", 500);
            }
        }

        rabbitMQSender.sendToNextHop(message, false);

    }

    protected RabbitMQSender getRabbitMQSender() {
        return rabbitMQSender;
    }

    public interface TaskExecutor {
        public void execute(CustomMessage context, TargetTasks triggeredBinding);

        public List<TargetTasks> getBinding();
    }
}
