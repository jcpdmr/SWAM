package com.swam.gateway;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.swam.commons.ApiTemplateVariables;
import com.swam.commons.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.RoutingInstructions.TargetMicroservices;
import com.swam.commons.Pair;
import com.swam.gateway.EndPoint.TargetType;

@Configuration
public class EndpointsConfig {

    // Define all workflows related API calls
    @Bean
    public EndPoint workflowEndpoint() {

        String BASE_PATTERN = "/api/workflow"
                + ApiTemplateVariables.TARGET_TYPE_PATTERN;

        EndPoint endPoint = EndPoint.builder()

                // Setting endpoint paths
                .setBindingPaths(List.of(
                        BASE_PATTERN,
                        BASE_PATTERN + ApiTemplateVariables.WORKFLOW_ID_PATTERN))

                // /catalog path
                .withTargetType(TargetType.CATALOG)
                .withMethod(HttpMethod.GET)
                .withOptionalIds(ApiTemplateVariables.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG,
                                TargetMessageHandler.GET_WORKFLOW)))
                .addMethod(HttpMethod.POST)
                .withForbiddenIds(ApiTemplateVariables.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG,
                                TargetMessageHandler.POST_WORKFLOW)))
                .addMethod(HttpMethod.PUT)
                .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG,
                                TargetMessageHandler.PUT_WORKFLOW)))
                .addMethod(HttpMethod.DELETE)
                .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG,
                                TargetMessageHandler.DELETE_WORKFLOW)))

                // /operation path
                .addTargetType(TargetType.OPERATION)
                .withMethod(HttpMethod.GET)
                .withOptionalIds(ApiTemplateVariables.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.OPERATION,
                                TargetMessageHandler.GET_WORKFLOW)))
                .addMethod(HttpMethod.PUT)
                .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.OPERATION,
                                TargetMessageHandler.PUT_WORKFLOW)))
                .addMethod(HttpMethod.DELETE)
                .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                .withRouting(List
                        .of(Pair.of(TargetMicroservices.OPERATION,
                                TargetMessageHandler.DELETE_WORKFLOW)))

                // /tobeinstantiated path
                .addTargetType(TargetType.TO_BE_ISTANTIATED)
                .withMethod(HttpMethod.GET)
                .withNeededIds(ApiTemplateVariables.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG,
                                TargetMessageHandler.ISTANCE_TEMPLATE),
                                Pair.of(
                                        TargetMicroservices.OPERATION,
                                        TargetMessageHandler.MAKE_PERSISTENCE)))

                .build();

        return endPoint;

    }

}
