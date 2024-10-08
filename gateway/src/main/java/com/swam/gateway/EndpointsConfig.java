package com.swam.gateway;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMicroservices;
import com.swam.commons.intercommunication.Pair;
import com.swam.gateway.EndPoint.TargetType;

@Configuration
public class EndpointsConfig {

    // Define all workflows related API calls
    @Bean
    public EndPoint workflowEndpoint() {

        String BASE_PATTERN = "/api/workflow"
                + ApiTemplateVariable.TARGET_TYPE_PATTERN;

        EndPoint endPoint = EndPoint.builder()

                // Setting endpoint paths
                .setBindingPaths(List.of(
                        BASE_PATTERN,
                        BASE_PATTERN + ApiTemplateVariable.WORKFLOW_ID_PATTERN))

                // /catalog path
                .withTargetType(TargetType.CATALOG)
                .withMethod(HttpMethod.GET)
                .withOptionalIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.WORKFLOW)))
                .addMethod(HttpMethod.POST)
                .withForbiddenIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.WORKFLOW)))
                .addMethod(HttpMethod.PUT)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.WORKFLOW)))
                .addMethod(HttpMethod.DELETE)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.WORKFLOW)))

                // /operation path
                .addTargetType(TargetType.OPERATION)
                .withMethod(HttpMethod.GET)
                .withOptionalIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.OPERATION, TargetMessageHandler.WORKFLOW)))
                .addMethod(HttpMethod.PUT)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.OPERATION, TargetMessageHandler.WORKFLOW)))
                .addMethod(HttpMethod.DELETE)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(List
                        .of(Pair.of(TargetMicroservices.OPERATION, TargetMessageHandler.WORKFLOW)))

                // /tobeinstantiated path
                .addTargetType(TargetType.TO_BE_ISTANTIATED)
                .withMethod(HttpMethod.GET)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.ISTANCE_TEMPLATE),
                                Pair.of(TargetMicroservices.OPERATION, TargetMessageHandler.MAKE_PERSISTENCE)))

                .build();

        return endPoint;

    }

    // Product related API
    @Bean
    public EndPoint productEndpoint() {

        String BASE_PATTERN = "/api/workflow"
                + ApiTemplateVariable.TARGET_TYPE_PATTERN + ApiTemplateVariable.WORKFLOW_ID_PATTERN
                + "/product";

        EndPoint endPoint = EndPoint.builder()

                // Setting endpoint paths
                .setBindingPaths(List.of(
                        BASE_PATTERN,
                        BASE_PATTERN + ApiTemplateVariable.PRODUCT_ID_PATTERN))

                // /catalog path
                .withTargetType(TargetType.CATALOG)
                .withMethod(HttpMethod.GET)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withOptionalIds(ApiTemplateVariable.PRODUCT_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.PRODUCT)))
                .addMethod(HttpMethod.PUT)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withNeededIds(ApiTemplateVariable.PRODUCT_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.PRODUCT)))
                .addMethod(HttpMethod.DELETE)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withNeededIds(ApiTemplateVariable.PRODUCT_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.PRODUCT)))

                // /operation path
                .addTargetType(TargetType.OPERATION)
                .withMethod(HttpMethod.GET)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withOptionalIds(ApiTemplateVariable.PRODUCT_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.OPERATION, TargetMessageHandler.PRODUCT)))
                .addMethod(HttpMethod.PUT)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withNeededIds(ApiTemplateVariable.PRODUCT_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.OPERATION, TargetMessageHandler.PRODUCT)))
                .addMethod(HttpMethod.DELETE)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withNeededIds(ApiTemplateVariable.PRODUCT_ID)
                .withRouting(List.of(Pair.of(TargetMicroservices.OPERATION, TargetMessageHandler.PRODUCT)))

                .build();

        return endPoint;

    }

    // Define all analysis related API calls
    @Bean
    public EndPoint analysisEndpoint() {

        String BASE_PATTERN = "/api/analysis"
                + ApiTemplateVariable.TARGET_TYPE_PATTERN;

        EndPoint endPoint = EndPoint.builder()

                // Setting endpoint paths
                .setBindingPaths(List.of(
                        BASE_PATTERN + ApiTemplateVariable.WORKFLOW_ID_PATTERN))

                // /catalog path
                .withTargetType(TargetType.CATALOG)
                .withMethod(HttpMethod.GET)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.CATALOG, TargetMessageHandler.FORWARD_WORKFLOW),
                                Pair.of(TargetMicroservices.ANALYSIS, TargetMessageHandler.ANALYZE_TEMPLATE)))

                // /operation path
                .addTargetType(TargetType.OPERATION)
                .withMethod(HttpMethod.GET)
                .withNeededIds(ApiTemplateVariable.WORKFLOW_ID)
                .withRouting(
                        List.of(Pair.of(TargetMicroservices.OPERATION, TargetMessageHandler.FORWARD_WORKFLOW),
                                Pair.of(TargetMicroservices.ANALYSIS, TargetMessageHandler.ANALYZE_INSTANCE)))

                .build();

        return endPoint;

    }
}
