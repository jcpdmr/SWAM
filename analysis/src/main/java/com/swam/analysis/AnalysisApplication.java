package com.swam.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.analysis" })
public class AnalysisApplication {

	private final RequestHandler requestHandler;

	public AnalysisApplication(RequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	public static void main(String[] args) {
		SpringApplication.run(AnalysisApplication.class, args);
	}

	// @RabbitListener(queues = "analysis_in")
	// public void messageHandler(CustomMessage msg, MessageProperties
	// messageProperties) {
	// System.out.println("Messaggio ricevuto dall'handler: " + msg.getMsg());
	// System.out.println("Messagge properties: " + messageProperties);

	// OrchestratorInfo orchestratorInfo = new OrchestratorInfo(messageProperties);

	// requestHandler.handle(orchestratorInfo.getTargetMethod());
	// rabbitMQSender.sendToNextHop(msg, false, orchestratorInfo);
	// }
}
