package com.swam.operation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.swam.commons.OrchestratorInfo.TargetMethods;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons" })
public class OperationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperationApplication.class, args);
    }

    @RabbitListener(queues = "operation_in")
    public void requestHandler(Message msg) {
        System.out.println("Messaggio ricevuto dall'handler: " + msg);

        // Map<TargetMethods, MethodInvoker> methodMap = new HashMap<>();

        // methodMap.put(TargetMethods.ANALYZE, OperationApplication::echo1);
    }

    @FunctionalInterface
    interface MethodInvoker {
        void invoke(OperationApplication obj);
    }

    public static void echo1() {
        System.out.println("method 1");
    }

    public static void echo2() {
        System.out.println("method 2");
    }
}
