package com.swam.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class Controller {

    private final Dispatcher dispatcher;

    public Controller(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @RequestMapping("/dev/**")
    public ResponseEntity<String> dispatch(HttpServletRequest request) {
        String path = request.getRequestURI();
        return dispatcher.dispatchRequest(path, request);
    }

    // @GetMapping(value = "/test-api-get")
    // public ResponseEntity<String> handleGETRequest(@RequestParam(required =
    // false) Map<String, String> requestParam) {
    // System.out.println("Request params: " + requestParam);

    // OrchestratorInfo orchestratorInfo = orchestrator.computeOrchestratorInfo();
    // CustomMessage testApiGET = new CustomMessage("test get", orchestratorInfo,
    // TargetMicroservices.GATEWAY,
    // MessageType.TO_BE_FORWARDED);
    // testApiGET.setRequestParam(Optional.of(requestParam));

    // rabbitMQSender.sendToNextHop(testApiGET, true);

    // return ResponseEntity.ok("api-get-ok");
    // }

    // @PostMapping("/test-api-post")
    // public ResponseEntity<String> handlePOSTRequest(@RequestBody Map<String,
    // Object> requestBody) {
    // System.out.println("Request body: " + requestBody);

    // OrchestratorInfo orchestratorInfo = orchestrator.computeOrchestratorInfo();
    // CustomMessage testApiPOST = new CustomMessage("test post", orchestratorInfo,
    // TargetMicroservices.GATEWAY,
    // MessageType.TO_BE_FORWARDED);
    // testApiPOST.setRequestBody(Optional.of(requestBody));

    // rabbitMQSender.sendToNextHop(testApiPOST, true);

    // return ResponseEntity.ok("api-post-ok");
    // }
}
