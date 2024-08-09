package com.swam.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.operation" })
public class OperationApplication {

    private final RequestHandler requestHandler;

    public OperationApplication(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(OperationApplication.class, args);
    }

    // @RabbitListener(queues = "operation_in")
    // public void messageHandler(CustomMessage msg, MessageProperties
    // messageProperties) {
    // System.out.println("Messaggio ricevuto dall'handler: " + msg.getMsg());
    // System.out.println("Messagge properties: " + messageProperties);

    // OrchestratorInfo orchestratorInfo = new OrchestratorInfo(messageProperties);

    // requestHandler.handle(orchestratorInfo.getTargetMethod());
    // rabbitMQSender.sendToNextHop(msg, false, orchestratorInfo);
    // }

}
