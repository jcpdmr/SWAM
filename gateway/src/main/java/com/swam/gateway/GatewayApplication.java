package com.swam.gateway;

import java.util.Date;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swam.commons.OrchestratorInfo;
import com.swam.commons.OrchestratorInfoBuilder;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

@SpringBootApplication
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.gateway" })
@RestController
public class GatewayApplication {

	private final RabbitMQSender rabbitMQSender;

	public GatewayApplication(RabbitMQSender rabbitMQSender) {
		this.rabbitMQSender = rabbitMQSender;
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	// @GetMapping(value = "/send")
	// public ResponseEntity<String> producer() {
	// System.out.println("Sending msg" + new Date().toString());

	// String msgToCatalog = "hello catalog from gateway: " + new Date().toString();
	// String msgToOperation = "hello operation from gateway: " + new
	// Date().toString();
	// String msgToAnalysis = "hello analysis from gateway: " + new
	// Date().toString();

	// rabbitMQSender.sendToCatalog(msgToCatalog, true);
	// rabbitMQSender.sendToAnalysis(msgToAnalysis);
	// rabbitMQSender.sendToOperation(msgToOperation);

	// return ResponseEntity.ok("msg sended");
	// }

	@GetMapping(value = "/testapi")
	public ResponseEntity<String> testAPI() {
		System.out.println("Sending msg" + new Date().toString());

		OrchestratorInfo orchestratorInfo = OrchestratorInfoBuilder.newBuild()
				.addTargets(TargetMicroservices.CATALOG, TargetMethods.ISTANCE_TEMPLATE)
				.addTargets(TargetMicroservices.OPERATION, TargetMethods.ANALYZE)
				.addTargets(TargetMicroservices.ANALYSIS, TargetMethods.NULL)
				.addTargets(TargetMicroservices.GATEWAY, TargetMethods.NULL)
				.build();

		Message testAPI = MessageBuilder.withBody("test msg".getBytes())
				.andProperties(orchestratorInfo.toMessageProperties())
				.build();

		rabbitMQSender.sendToNextHop(testAPI, true);

		return ResponseEntity.ok("sended to catalog");
	}

	@RabbitListener(queues = "gateway_in")
	public void messageHandler(Message msg) {
		System.out.println("Messaggio ricevuto dall'handler: " + msg);
		System.out.println("testo del messaggio: " + new String(msg.getBody()));
	}
}
