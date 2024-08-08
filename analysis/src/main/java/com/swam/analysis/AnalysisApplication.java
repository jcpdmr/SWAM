package com.swam.analysis;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.swam.commons.OrchestratorInfo;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.RequestHandler;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.analysis" })
public class AnalysisApplication {

	private final RabbitMQSender rabbitMQSender;

	private final RequestHandler requestHandler;

	public AnalysisApplication(@Qualifier("analysis") RequestHandler requestHandler,
			RabbitMQSender rabbitMQSenderMicroservices) {
		this.rabbitMQSender = rabbitMQSenderMicroservices;
		this.requestHandler = requestHandler;
	}

	public static void main(String[] args) {
		SpringApplication.run(AnalysisApplication.class, args);
	}

	@RabbitListener(queues = "analysis_in")
	public void messageHandler(Message msg) {
		System.out.println("Messaggio ricevuto dall'handler: " + msg);

		requestHandler.handle(new OrchestratorInfo(msg.getMessageProperties()).getTargetMethod());
		rabbitMQSender.sendToNextHop(msg, false);
	}
}
