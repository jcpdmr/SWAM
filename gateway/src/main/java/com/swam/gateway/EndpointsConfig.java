package com.swam.gateway;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.swam.commons.ApiTemplateVariables;
import com.swam.commons.CustomMessage;
import com.swam.commons.RoutingInstructions.TargetTasks;
import com.swam.commons.RoutingInstructions.TargetMicroservices;
import com.swam.commons.Pair;
import com.swam.gateway.EndPoint.TargetType;

@Configuration
public class EndpointsConfig {

    // Handle all workflows related API calls
    @Bean
    public AbstractEndPointHandler workflowEndpoint() {
        return new AbstractEndPointHandler() {

            private final static String BASE_PATTERN = "/api/workflow"
                    + ApiTemplateVariables.TARGET_TYPE_PATTERN;

            @Override
            public List<String> getBindingPaths() {
                return List.of(
                        BASE_PATTERN,
                        BASE_PATTERN + ApiTemplateVariables.WORKFLOW_ID_PATTERN);
            }

            @Override
            public CustomMessage handle(HttpMethod httpMethod, Map<String, String> uriTemplateVariables,
                    Optional<Map<String, String>> requestParams, Optional<String> requestBody) {

                EndPoint endPoint = EndPoint.builder()

                        // /catalog path
                        .withTargetType(TargetType.CATALOG)
                        .withMethod(HttpMethod.GET)
                        .withOptionalIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(
                                List.of(Pair.of(TargetMicroservices.CATALOG,
                                        TargetTasks.GET_WORKFLOW)))
                        .addMethod(HttpMethod.POST)
                        .withForbiddenIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(
                                List.of(Pair.of(TargetMicroservices.CATALOG,
                                        TargetTasks.POST_WORKFLOW)))
                        .addMethod(HttpMethod.PUT)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(
                                List.of(Pair.of(TargetMicroservices.CATALOG,
                                        TargetTasks.PUT_WORKFLOW)))
                        .addMethod(HttpMethod.DELETE)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(
                                List.of(Pair.of(TargetMicroservices.CATALOG,
                                        TargetTasks.DELETE_WORKFLOW)))

                        // /operation path
                        .addTargetType(TargetType.OPERATION)
                        .withMethod(HttpMethod.GET)
                        .withOptionalIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(
                                List.of(Pair.of(TargetMicroservices.OPERATION,
                                        TargetTasks.GET_WORKFLOW)))
                        .addMethod(HttpMethod.PUT)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(
                                List.of(Pair.of(TargetMicroservices.OPERATION,
                                        TargetTasks.PUT_WORKFLOW)))
                        .addMethod(HttpMethod.DELETE)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(List
                                .of(Pair.of(TargetMicroservices.OPERATION,
                                        TargetTasks.DELETE_WORKFLOW)))

                        // /tobeinstantiated path
                        .addTargetType(TargetType.TO_BE_ISTANTIATED)
                        .withMethod(HttpMethod.GET)
                        .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                        .withRouting(
                                List.of(Pair.of(TargetMicroservices.CATALOG,
                                        TargetTasks.ISTANCE_TEMPLATE),
                                        Pair.of(
                                                TargetMicroservices.OPERATION,
                                                TargetTasks.MAKE_PERSISTENCE)))

                        .build();

                // System.out.println(endPoint.getEndPointData());

                return buildMessage(endPoint, httpMethod, uriTemplateVariables, requestParams,
                        requestBody);

            }

        };
    }

}
