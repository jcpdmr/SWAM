package com.swam.gateway;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.CustomMessage.MessageType;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

@RestController
public class Controller {

    private final Orchestrator orchestrator;
    private final RabbitMQSender rabbitMQSender;

    public Controller(Orchestrator orchestrator, RabbitMQSender rabbitMQSender) {
        this.orchestrator = orchestrator;
        this.rabbitMQSender = rabbitMQSender;
    }

    @GetMapping(value = "/test-api-get")
    public ResponseEntity<String> handleGETRequest(@RequestParam(required = false) Map<String, String> requestParam) {
        System.out.println("Request params: " + requestParam);

        OrchestratorInfo orchestratorInfo = orchestrator.computeOrchestratorInfo();
        CustomMessage testApiGET = new CustomMessage("test get", orchestratorInfo, TargetMicroservices.GATEWAY,
                MessageType.TO_BE_FORWARDED);
        testApiGET.setRequestParam(Optional.of(requestParam));

        rabbitMQSender.sendToNextHop(testApiGET, true);

        return ResponseEntity.ok("api-get-ok");
    }

    @PostMapping("/test-api-post")
    public ResponseEntity<String> handlePOSTRequest(@RequestBody Map<String, Object> requestBody) {
        System.out.println("Request body: " + requestBody);

        OrchestratorInfo orchestratorInfo = orchestrator.computeOrchestratorInfo();
        CustomMessage testApiPOST = new CustomMessage("test post", orchestratorInfo, TargetMicroservices.GATEWAY,
                MessageType.TO_BE_FORWARDED);
        testApiPOST.setRequestBody(Optional.of(requestBody));

        rabbitMQSender.sendToNextHop(testApiPOST, true);

        return ResponseEntity.ok("api-post-ok");
    }
}
