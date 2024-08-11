package com.swam.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;
import com.swam.commons.CustomMessage.MessageType;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;
import com.swam.commons.RabbitMQSender;
import com.swam.gateway.OrchestratorConfig.OrchestrationPlanner;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class Dispatcher {

    private final Map<String, OrchestrationPlanner> orchestratorMap;
    private final RabbitMQSender rabbitMQSender;
    Map<UUID, OrchestratorInfo> activeOrchestration;

    public Dispatcher(List<OrchestrationPlanner> orchestratorSuppliers, RabbitMQSender rabbitMQSender) {
        this.orchestratorMap = orchestratorSuppliers.stream()
                .collect(Collectors.toMap(OrchestrationPlanner::getBindingPath, Function.identity()));
        this.rabbitMQSender = rabbitMQSender;
        this.activeOrchestration = new HashMap<>();
    }

    public ResponseEntity<String> dispatchRequest(String path, HttpServletRequest request) {

        // TODO: improve path resolution (security problems)
        System.out.println(path);
        String relativePath = path.substring(path.indexOf("/dev/") + "/dev/".length());

        OrchestrationPlanner planner = orchestratorMap.get(relativePath);

        if (planner != null) {
            OrchestratorInfo orchestratorInfo = planner.orchestrate();
            activeOrchestration.put(orchestratorInfo.getUuid(), orchestratorInfo);

            CustomMessage testApi = new CustomMessage("test api", orchestratorInfo,
                    TargetMicroservices.GATEWAY,
                    MessageType.TO_BE_FORWARDED);

            String requestMethod = request.getMethod();
            if (requestMethod.equalsIgnoreCase("POST")) {

                String contentType = request.getContentType();
                if ("application/json".equalsIgnoreCase(contentType)) {
                    // Read json body
                    StringBuilder stringBuilder = new StringBuilder();
                    try {
                        BufferedReader bufferedReader = request.getReader();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                    } catch (IOException e) {
                        // TODO: handle exception
                    }
                    String requestBody = stringBuilder.toString();
                    testApi.setRequestBody(Optional.of(requestBody));
                } else {
                    return ResponseEntity.badRequest().body("ContenteType: " + contentType + " not supported");
                }

            } else if (requestMethod.equalsIgnoreCase("GET")) {
                Map<String, String[]> paramMap = request.getParameterMap();
                Map<String, List<String>> transformedMap = paramMap.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> Arrays.asList(e.getValue())));

                testApi.setParamMap(Optional.of(transformedMap));
            } else {
                return ResponseEntity.badRequest().body("Method: " + requestMethod + " not supported");
            }

            rabbitMQSender.sendToNextHop(testApi, true);

            return ResponseEntity.ok("Request handled correctly");
        } else {
            return ResponseEntity.badRequest().body("Path: " + path + " not found");

        }
    }

    public Optional<OrchestratorInfo> getActiveOrchestratorInfo(UUID orchestrationUUID) {
        if (activeOrchestration.containsKey(orchestrationUUID)) {
            return Optional.of(activeOrchestration.get(orchestrationUUID));
        } else {
            return Optional.empty();
        }
    }

    public Boolean removeActiveOrchestration(UUID orchestrationUUID) {
        if (activeOrchestration.containsKey(orchestrationUUID)) {
            activeOrchestration.remove(orchestrationUUID);
            return true;
        } else {
            return false;
        }
    }

}
