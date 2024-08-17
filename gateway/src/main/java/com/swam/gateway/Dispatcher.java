package com.swam.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.RabbitMQSender;

@Service
public class Dispatcher {

    private final Map<List<String>, AbstractEndPointHandler> endPointMap;
    private final RabbitMQSender rabbitMQSender;
    private final Map<UUID, OrchestratorInfo> activeOrchestration;
    private final AntPathMatcher antPathMatcher;

    public Dispatcher(List<AbstractEndPointHandler> orchestratorSuppliers, RabbitMQSender rabbitMQSender) {
        this.endPointMap = orchestratorSuppliers.stream()
                .collect(Collectors.toMap(AbstractEndPointHandler::getBindingPaths, Function.identity()));
        this.rabbitMQSender = rabbitMQSender;
        this.activeOrchestration = new HashMap<>();
        this.antPathMatcher = new AntPathMatcher();
    }

    public ResponseEntity<Object> dispatchRequest(HttpMethod httpMethod, String uriPath,
            Optional<Map<String, String>> requestParams,
            Optional<String> requestBody) {

        CustomMessage apiMessage = null;
        // Look for a match in all bindingPaths provided by AbstractEndPointHandler's
        // Beans
        for (Entry<List<String>, AbstractEndPointHandler> entry : endPointMap.entrySet()) {
            for (String bindingPath : entry.getKey()) {
                // System.out.println("Check bindingPath: " + bindingPath);
                if (antPathMatcher.match(bindingPath, uriPath)) {
                    // If a match is found, uriVariables will be extracted (they are defined in the
                    // bindingPath itself)
                    Map<String, String> variables = antPathMatcher.extractUriTemplateVariables(
                            bindingPath,
                            uriPath);
                    apiMessage = entry.getValue().handle(httpMethod, variables, requestParams,
                            requestBody);
                }
            }
        }

        // Check if any Errors occured during Orchestration and forward it
        if (apiMessage == null) {
            return new ResponseEntity<>("Path: " + uriPath + " not found", HttpStatusCode.valueOf(404));
        } else if (apiMessage.getResponseEntity().getStatusCode().isError()) {
            return apiMessage.getResponseEntity();
        }

        // Save orchestration as an active orchestration (related to a request that need
        // to be resolved)
        activeOrchestration.put(apiMessage.getOrchestratorInfo().getUuid(), apiMessage.getOrchestratorInfo());

        rabbitMQSender.sendToNextHop(apiMessage, true);

        return ResponseEntity.ok("Request handled correctly");

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
