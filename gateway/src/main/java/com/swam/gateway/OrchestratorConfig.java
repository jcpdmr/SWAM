package com.swam.gateway;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
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

import lombok.Getter;
import lombok.Setter;

import com.swam.commons.OrchestratorInfoBuilder;
import com.swam.gateway.EndPoint.MethodInfo;
import com.swam.gateway.EndPoint.Requirement;
import com.swam.gateway.EndPoint.TargetType;

@Configuration
public class OrchestratorConfig {
    @Setter
    @Getter
    public abstract class OrchestrationPlanner {

        private Map<String, String> uriTemplateVariables;
        private Optional<Map<String, String>> requestParams;
        private Optional<String> requestBody;

        // TODO: this should be used in all Beans inside orchestrate() (maybe there is a
        // method to force it)
        private void setRequestEntities(Map<String, String> uriTemplateVariables,
                Optional<Map<String, String>> requestParams, Optional<String> requestBody) {
            this.uriTemplateVariables = uriTemplateVariables;
            this.requestParams = requestParams;
            this.requestBody = requestBody;
        }

        // Should return a list of uriPath to with the bean will be bound
        public abstract List<String> getBindingPaths();

        // Main method called on each beans by Dispatcher
        public CustomMessage orchestrate(HttpMethod httpMethod, Map<String, String> uriTemplateVariables,
                Optional<Map<String, String>> requestParams, Optional<String> requestBody) {
            setRequestEntities(uriTemplateVariables, requestParams, requestBody);

            return null;
        }

        // Perform conversion to TargetType
        private Optional<TargetType> convertToTargetType(String type) {
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

        protected CustomMessage buildMessage(EndPoint endPoint, HttpMethod httpMethod) {

            // Check if targetType is valid and defined in the current endPoint
            String type = uriTemplateVariables.get(ApiTemplateVariables.TARGET_TYPE);

            TargetType targetType;
            if (convertToTargetType(type).isEmpty()) {
                return buildErrorResponse(
                        "Target type: \"" + type + "\" not valid, valid types are: "
                                + endPoint.getEndPointData().keySet(),
                        400);
            } else {
                targetType = convertToTargetType(type).get();
            }

            if (!endPoint.getEndPointData().containsKey(targetType)) {
                return buildErrorResponse(
                        "Target type: \"" + type + "\" not allowed, allowed types are: "
                                + endPoint.getEndPointData().keySet(),
                        400);
            }

            // Check if method is allowed

            if (!endPoint.getEndPointData().get(targetType).containsKey(httpMethod)) {
                return buildErrorResponse(
                        "Method type: \"" + httpMethod + "\" not allowed, allowed methods are: "
                                + endPoint.getEndPointData().get(targetType).keySet(),
                        400);
            }

            MethodInfo methodInfo = endPoint.getEndPointData().get(targetType).get(httpMethod);

            // Check needed ids requirements
            if (methodInfo.getIdsRequirementsMap().containsKey(Requirement.NEEDED)) {
                for (String id : methodInfo.getIdsRequirementsMap().get(Requirement.NEEDED)) {
                    if (uriTemplateVariables.get(id) == null) {
                        return buildErrorResponse("Field: \"" + id + "\" required", 400);
                    }
                }
            }

            // Check forbidden ids requirements
            if (methodInfo.getIdsRequirementsMap().containsKey(Requirement.FORBIDDEN)) {
                for (String id : methodInfo.getIdsRequirementsMap().get(Requirement.FORBIDDEN)) {
                    if (uriTemplateVariables.get(id) != null) {
                        return buildErrorResponse("Field: \"" + id + "\" not allowed", 400);
                    }
                }
            }

            // build orchestration
            OrchestratorInfo orchestratorInfo = OrchestratorInfoBuilder.newBuild()
                    .setTargets(methodInfo.getRoutingMap()).build();

            return buildFinalMessage(orchestratorInfo, ResponseEntity.ok(null));

        }

        private CustomMessage buildErrorResponse(String errorMsg, Integer httpStatusCode) {
            return buildFinalMessage(null,
                    new ResponseEntity<>(
                            errorMsg,
                            HttpStatusCode.valueOf(httpStatusCode)));
        }

        private CustomMessage buildFinalMessage(OrchestratorInfo orchestratorInfo,
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

                super.orchestrate(httpMethod, uriTemplateVariables, requestParams, requestBody);

                EndPoint endPoint = EndPoint.builder()
                        .withTargetType(TargetType.CATALOG)
                        .withMethod(HttpMethod.GET)
                        .withOptionalIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List.of(Pair.of(TargetMicroservices.CATALOG, TargetMethods.GET_WORKFLOW)))
                        .addMethod(HttpMethod.POST)
                        .withForbiddenIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List.of(Pair.of(TargetMicroservices.CATALOG, TargetMethods.POST_WORKFLOW)))
                        .addMethod(HttpMethod.PUT)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List.of(Pair.of(TargetMicroservices.CATALOG, TargetMethods.PUT_WORKFLOW)))
                        .addMethod(HttpMethod.DELETE)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List.of(Pair.of(TargetMicroservices.CATALOG, TargetMethods.DELETE_WORKFLOW)))

                        .addTargetType(TargetType.OPERATION)
                        .withMethod(HttpMethod.GET)
                        .withOptionalIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List.of(Pair.of(TargetMicroservices.OPERATION, TargetMethods.GET_WORKFLOW)))
                        .addMethod(HttpMethod.PUT)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List.of(Pair.of(TargetMicroservices.OPERATION, TargetMethods.PUT_WORKFLOW)))
                        .addMethod(HttpMethod.DELETE)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List.of(Pair.of(TargetMicroservices.OPERATION, TargetMethods.DELETE_WORKFLOW)))

                        .addTargetType(TargetType.TO_BE_ISTANTIATED)
                        .withMethod(HttpMethod.GET)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List.of(Pair.of(TargetMicroservices.CATALOG, TargetMethods.ISTANCE_TEMPLATE),
                                Pair.of(TargetMicroservices.OPERATION, TargetMethods.MAKE_PERSISTENCE)))

                        .build();

                // System.out.println(endPoint.getEndPointData());

                return buildMessage(endPoint, httpMethod);

            }

        };
    }

}
