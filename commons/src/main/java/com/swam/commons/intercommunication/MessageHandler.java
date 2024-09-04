package com.swam.commons.intercommunication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.MongodbDTO;

public interface MessageHandler {
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding)
            throws ProcessingMessageException;

    public List<TargetMessageHandler> getBinding();

    public default String getUriId(CustomMessage context, ApiTemplateVariable apiTemplateVariable,
            Boolean isRequired)
            throws ProcessingMessageException {
        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String uriId = uriTemplateVariables.get(apiTemplateVariable.value());
        if (isRequired && uriId == null) {
            throw new ProcessingMessageException(context.getRequestMethod() + " with null " + apiTemplateVariable,
                    "Internal server error", 500);
        }
        return uriId;

    }

    public default Boolean isParamSpecified(CustomMessage context, String paramKey, String paramValue) {
        if (context.getRequestParams().isPresent()) {
            Map<String, String> paramsMap = context.getRequestParams().get();
            if (paramsMap.containsKey(paramKey) && paramsMap.get(paramKey).equals(paramValue)) {
                return true;
            }
        }
        return false;
    }

    public default <DTO extends MongodbDTO<?>, T> T convertResponseBody(Object responseBody,
            Class<DTO> clazz, Boolean toDTO) throws ProcessingMessageException {
        return uncheckedCast(
                convertBody((String) responseBody, clazz, "Internal server error", 500, toDTO));
    }

    public default <DTO extends MongodbDTO<?>, T> T convertRequestBody(Optional<String> requestBody,
            Class<DTO> clazz, Boolean toDTO) throws ProcessingMessageException {
        // RequestBody Check
        if (requestBody.isEmpty()) {
            throw new ProcessingMessageException("Error request with empty body",
                    "Error: request with empty body", 400);
        }
        return uncheckedCast(
                convertBody(requestBody.get(), clazz, "Error: request with empty body", 400, toDTO));
    }

    private <DTO extends MongodbDTO<?>> Object convertBody(String body,
            Class<DTO> clazz, String responseErrorMsg, Integer httpStatusCode, Boolean toDTO)
            throws ProcessingMessageException {

        // DTO deserialization
        DTO receivedDTO = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            receivedDTO = objectMapper.readValue(body, clazz);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    responseErrorMsg, httpStatusCode);
        }

        // DTO convertion and validation
        if (receivedDTO == null) {
            throw new ProcessingMessageException(
                    "Error DTO of type: " + clazz + " is null",
                    responseErrorMsg, httpStatusCode);
        } else {
            Object convertedObject = receivedDTO.convertAndValidate();
            if (toDTO) {
                return receivedDTO;
            } else {
                return convertedObject;
            }

        }
    }

    @SuppressWarnings("unchecked")
    private <T> T uncheckedCast(Object objectToCast) {
        return (T) objectToCast;
    }

}