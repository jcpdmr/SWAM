package com.swam.gateway;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.RoutingInstructions;
import com.swam.commons.intercommunication.RoutingInstructionsBuilder;
import com.swam.commons.intercommunication.CustomMessage.MessageType;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMicroservices;
import com.swam.gateway.EndPoint.MethodInfo;
import com.swam.gateway.EndPoint.Requirement;
import com.swam.gateway.EndPoint.TargetType;

import lombok.Getter;
import lombok.Setter;

@Service
@Setter
@Getter
public class MessageInitializer {

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

    public CustomMessage buildMessage(EndPoint endPoint, HttpMethod httpMethod,
            Map<String, String> uriTemplateVariables,
            Optional<Map<String, String>> requestParams, Optional<String> requestBody) {

        // Check if targetType is valid and defined in the current endPoint
        String type = uriTemplateVariables.get(ApiTemplateVariable.TARGET_TYPE.value());

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
            for (ApiTemplateVariable requiredId : methodInfo.getIdsRequirementsMap().get(Requirement.NEEDED)) {
                if (uriTemplateVariables.get(requiredId.value()) == null) {
                    return buildErrorResponse("Field: \"" + requiredId + "\" required", 400);
                }
            }
        }

        // Check forbidden ids requirements
        if (methodInfo.getIdsRequirementsMap().containsKey(Requirement.FORBIDDEN)) {
            for (ApiTemplateVariable forbiddenId : methodInfo.getIdsRequirementsMap().get(Requirement.FORBIDDEN)) {
                if (uriTemplateVariables.get(forbiddenId.value()) != null) {
                    return buildErrorResponse("Field: \"" + forbiddenId + "\" not allowed", 400);
                }
            }
        }

        // build orchestration
        RoutingInstructions routingInstructions = RoutingInstructionsBuilder.newBuild()
                .setTargets(methodInfo.getRoutingMap()).build();

        return buildFinalMessage(routingInstructions, ResponseEntity.ok(null), MessageType.TO_BE_FORWARDED,
                uriTemplateVariables, httpMethod, requestParams,
                requestBody);

    }

    private CustomMessage buildErrorResponse(String errorMsg, Integer httpStatusCode) {
        return buildFinalMessage(null,
                new ResponseEntity<>(
                        errorMsg,
                        HttpStatusCode.valueOf(httpStatusCode)),
                MessageType.ERROR,
                null, null, null, null);
    }

    private CustomMessage buildFinalMessage(
            RoutingInstructions routingInstructions,
            ResponseEntity<Object> responseEntity,
            MessageType messageType,
            Map<String, String> uriTemplateVariables,
            HttpMethod httpMethod,
            Optional<Map<String, String>> requestParams,
            Optional<String> requestBody) {

        CustomMessage apiMessage = new CustomMessage("test api", routingInstructions,
                TargetMicroservices.GATEWAY,
                messageType, responseEntity);
        apiMessage.setUriTemplateVariables(uriTemplateVariables);
        apiMessage.setRequestMethod(httpMethod);
        apiMessage.setRequestParams(requestParams);
        apiMessage.setRequestBody(requestBody);

        return apiMessage;
    }
}
