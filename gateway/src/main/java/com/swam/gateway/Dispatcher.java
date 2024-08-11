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
    private final Map<UUID, OrchestratorInfo> activeOrchestration;

    public Dispatcher(List<OrchestrationPlanner> orchestratorSuppliers, RabbitMQSender rabbitMQSender) {
        this.orchestratorMap = orchestratorSuppliers.stream()
                .collect(Collectors.toMap(OrchestrationPlanner::getBindingPath, Function.identity()));
        this.rabbitMQSender = rabbitMQSender;
        this.activeOrchestration = new HashMap<>();
    }

    public ResponseEntity<String> dispatchRequest(String path, Optional<Map<String, String>> queryParams,
            Optional<String> requestBody) {

        OrchestrationPlanner planner = orchestratorMap.get(path);

        if (planner != null) {
            OrchestratorInfo orchestratorInfo = planner.orchestrate();
            activeOrchestration.put(orchestratorInfo.getUuid(), orchestratorInfo);

            CustomMessage testApi = new CustomMessage("test api", orchestratorInfo,
                    TargetMicroservices.GATEWAY,
                    MessageType.TO_BE_FORWARDED);

            if (queryParams.isPresent()) {
                testApi.setQueryParams(Optional.of(queryParams.get()));
            }
            if (requestBody.isPresent()) {
                testApi.setRequestBody(Optional.of(requestBody.get()));
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
