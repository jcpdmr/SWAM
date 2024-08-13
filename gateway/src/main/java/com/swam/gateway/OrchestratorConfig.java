package com.swam.gateway;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.swam.commons.CustomMessage;
import com.swam.commons.CustomMessage.MessageType;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.swam.commons.OrchestratorInfoBuilder;

@Configuration
public class OrchestratorConfig {
    @Setter
    @Getter
    public abstract class OrchestrationPlanner {
        // To specify request target
        enum TargetType {
            CATALOG,
            OPERATION,
            TO_BE_ISTANTIATED
        }

        // To define if a patternId is needed/optional/forbidden for a particular method
        enum Requirement {
            NEEDED,
            OPTIONAL,
            FORBIDDEN
        }

        private Map<String, String> uriTemplateVariables;
        private Optional<Map<String, String>> requestParams;
        private Optional<String> requestBody;

        // TODO: this should be used in all Beans inside orchestrate() (maybe there is a
        // method to force it)
        protected void setRequestEntities(Map<String, String> uriTemplateVariables,
                Optional<Map<String, String>> requestParams, Optional<String> requestBody) {
            this.uriTemplateVariables = uriTemplateVariables;
            this.requestParams = requestParams;
            this.requestBody = requestBody;
        }

        // Should return a list of uriPath to with the bean will be bound
        public abstract List<String> getBindingPaths();

        // Main method called on each beans by Dispatcher
        public abstract CustomMessage orchestrate(HttpMethod httpMethod, Map<String, String> uriTemplateVariables,
                Optional<Map<String, String>> requestParams, Optional<String> requestBody);

        protected CustomMessage buildErrorResponse(String errorMsg, Integer httpStatusCode) {
            return buildFinalMessage(null,
                    new ResponseEntity<>(
                            errorMsg,
                            HttpStatusCode.valueOf(httpStatusCode)));
        }

        // Perform conversion to TargetType
        protected Optional<TargetType> convertToTargetType(String type) {
            if (type.equalsIgnoreCase("CATALOG")) {
                return Optional.of(TargetType.CATALOG);
            } else if (type.equalsIgnoreCase("OPERATION")) {
                return Optional.of(TargetType.OPERATION);
            } else if (type.equalsIgnoreCase("TOBEISTANTIATED")) {
                return Optional.of(TargetType.TO_BE_ISTANTIATED);
            } else {
                return Optional.empty();
            }
        }

        // wrapper Object to contains route orchestration
        @Setter
        @Getter
        @AllArgsConstructor
        protected class OrchestrationRoute {

            private final TargetType targetType;
            private final HttpMethod httpMethod;
            private final List<TargetMicroservices> targetMicroservices;
            private final List<TargetMethods> targetMethods;

        }

        protected CustomMessage buildMessageWithInstructions(TargetType targetType, HttpMethod httpMethod,
                Map<String, String> ids,
                Map<TargetType, Set<HttpMethod>> allowedMethodsForType,
                Map<HttpMethod, Map<Requirement, Set<String>>> idsRequirementsForMethod,
                List<OrchestrationRoute> routes) {

            // Check if method is allowed
            if (!allowedMethodsForType.get(targetType).contains(httpMethod)) {
                return buildErrorResponse("Method not allowed: " + httpMethod, 404);
            }
            // Check if ids requirements are met
            for (Entry<String, String> id : ids.entrySet()) {
                if (id.getValue() == null) {
                    // If any id needed is null, generate error response
                    if (idsRequirementsForMethod.get(httpMethod).get(Requirement.NEEDED).contains(id.getValue())) {
                        return buildErrorResponse("Missing needed id: " + id.getKey(), 400);
                    }
                } else {
                    // If any id forbidden is not null, generate error response
                    if (idsRequirementsForMethod.get(httpMethod).get(Requirement.FORBIDDEN).contains(id.getValue())) {
                        return buildErrorResponse("Not needed id included: " + id.getKey(), 400);
                    }
                }
            }

            OrchestratorInfo orchestratorInfo;
            // TODO: improve using Hashmap or refactoring
            // Search for specific route
            for (OrchestrationRoute orchestrationRoute : routes) {
                if (orchestrationRoute.getTargetType().equals(targetType)
                        && orchestrationRoute.getHttpMethod().equals(httpMethod)) {
                    orchestratorInfo = buildOrchestratorInfoFromRoute(orchestrationRoute);
                    return buildFinalMessage(orchestratorInfo, ResponseEntity.ok(null));
                }
            }

            // TODO: generate errore response for route not found
            return null;

        }

        protected OrchestratorInfo buildOrchestratorInfoFromRoute(OrchestrationRoute orchestrationRoute) {
            OrchestratorInfoBuilder orchestratorInfoBuilder = OrchestratorInfoBuilder.newBuild();
            List<TargetMicroservices> targetMicroservices = orchestrationRoute.getTargetMicroservices();
            List<TargetMethods> targetMethods = orchestrationRoute.getTargetMethods();
            for (int i = 0; i < targetMicroservices.size(); i++) {
                orchestratorInfoBuilder.addTargets(targetMicroservices.get(i), targetMethods.get(i));
            }
            return orchestratorInfoBuilder.build();
        }

        protected CustomMessage buildFinalMessage(OrchestratorInfo orchestratorInfo,
                ResponseEntity<String> responseEntity) {
            CustomMessage apiMessage = new CustomMessage("test api", orchestratorInfo,
                    TargetMicroservices.GATEWAY,
                    MessageType.TO_BE_FORWARDED, responseEntity);
            apiMessage.setUriTemplateVariables(uriTemplateVariables);
            apiMessage.setRequestParams(requestParams);
            apiMessage.setRequestBody(requestBody);

            return apiMessage;
        }

    }

    // Handle all workflows related API calls
    @Bean
    public OrchestrationPlanner workflowOrchestration() {
        return new OrchestrationPlanner() {

            private final static String BASE_PATTERN = "/api/workflow" + ApiTemplateVariables.TARGET_TYPE_PATTERN;

            @Override
            public List<String> getBindingPaths() {
                return List.of(
                        BASE_PATTERN,
                        BASE_PATTERN + ApiTemplateVariables.WORKFLOW_ID_PATTERN);
            }

            @Override
            public CustomMessage orchestrate(HttpMethod httpMethod, Map<String, String> uriTemplateVariables,
                    Optional<Map<String, String>> requestParams, Optional<String> requestBody) {

                setRequestEntities(uriTemplateVariables, requestParams, requestBody);

                String type = uriTemplateVariables.get(ApiTemplateVariables.TARGET_TYPE);
                String workflowId = uriTemplateVariables.get(ApiTemplateVariables.WORKFLOW_ID);

                TargetType targetType;
                if (convertToTargetType(type).isEmpty()) {
                    return buildErrorResponse(
                            "Target type: [" + type + "] not valid, valid types are: template or concrete", 400);
                } else {
                    targetType = convertToTargetType(type).get();
                }

                buildMessageWithInstructions(targetType, httpMethod,
                        Map.of(ApiTemplateVariables.WORKFLOW_ID, workflowId),
                        Map.of(
                                TargetType.CATALOG,
                                Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE),
                                TargetType.OPERATION,
                                Set.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE),
                                TargetType.TO_BE_ISTANTIATED,
                                Set.of(HttpMethod.GET)),
                        Map.of(
                                HttpMethod.GET, Map.of(Requirement.OPTIONAL, Set.of(workflowId)),
                                HttpMethod.POST, Map.of(Requirement.FORBIDDEN, Set.of(workflowId)),
                                HttpMethod.PUT, Map.of(Requirement.NEEDED, Set.of(workflowId)),
                                HttpMethod.DELETE, Map.of(Requirement.NEEDED, Set.of(workflowId))),
                        List.of(
                                new OrchestrationRoute(TargetType.CATALOG, HttpMethod.GET,
                                        List.of(TargetMicroservices.CATALOG), List.of(TargetMethods.GET_WORKFLOW)),
                                new OrchestrationRoute(TargetType.CATALOG, HttpMethod.POST,
                                        List.of(TargetMicroservices.CATALOG), List.of(TargetMethods.POST_WORKFLOW)),
                                new OrchestrationRoute(TargetType.CATALOG, HttpMethod.PUT,
                                        List.of(TargetMicroservices.CATALOG), List.of(TargetMethods.PUT_WORKFLOW)),
                                new OrchestrationRoute(TargetType.CATALOG, HttpMethod.DELETE,
                                        List.of(TargetMicroservices.CATALOG), List.of(TargetMethods.DELETE_WORKFLOW)),
                                new OrchestrationRoute(TargetType.OPERATION, HttpMethod.GET,
                                        List.of(TargetMicroservices.OPERATION), List.of(TargetMethods.GET_WORKFLOW)),
                                new OrchestrationRoute(TargetType.OPERATION, HttpMethod.PUT,
                                        List.of(TargetMicroservices.OPERATION), List.of(TargetMethods.PUT_WORKFLOW)),
                                new OrchestrationRoute(TargetType.OPERATION, HttpMethod.DELETE,
                                        List.of(TargetMicroservices.OPERATION), List.of(TargetMethods.DELETE_WORKFLOW)),
                                new OrchestrationRoute(TargetType.TO_BE_ISTANTIATED, HttpMethod.GET,
                                        List.of(TargetMicroservices.CATALOG, TargetMicroservices.OPERATION),
                                        List.of(TargetMethods.ISTANCE_TEMPLATE, TargetMethods.MAKE_PERSISTENCE))));

                return null;

            }

        };
    }

}
