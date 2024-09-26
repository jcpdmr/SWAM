package com.swam.commons.intercommunication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.MongodbTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MessageHandler {

    private final List<TargetMessageHandler> bindings;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract void handle(CustomMessage context, TargetMessageHandler triggeredBinding)
            throws ProcessingMessageException;

    public List<TargetMessageHandler> getBinding() {
        return bindings;
    }

    protected String getUriId(CustomMessage context, ApiTemplateVariable apiTemplateVariable,
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

    protected Boolean isParamSpecified(CustomMessage context, ApiTemplateVariable paramKey,
            ApiTemplateVariable paramValue) {
        if (context.getRequestParams().isPresent()) {
            Map<String, String> paramsMap = context.getRequestParams().get();
            if (paramsMap.containsKey(paramKey.value()) && paramsMap.get(paramKey.value()).equals(paramValue.value())) {
                return true;
            }
        }
        return false;
    }

    protected Optional<String> getParamValue(CustomMessage context, ApiTemplateVariable paramKey) {
        if (context.getRequestParams().isPresent()) {
            Map<String, String> paramsMap = context.getRequestParams().get();
            if (paramsMap.containsKey(paramKey.value())) {
                return Optional.of(paramsMap.get(paramKey.value()));
            }
        }
        return Optional.empty();
    }

    protected <TO extends MongodbTO<?>, T> T convertResponseBody(Object responseBody,
            Class<TO> clazz, Boolean toTO) throws ProcessingMessageException {
        return uncheckedCast(
                convertBody((String) responseBody, clazz, "Internal server error", 500, toTO));
    }

    protected <TO extends MongodbTO<?>, T> T convertRequestBody(Optional<String> requestBody,
            Class<TO> clazz, Boolean toTO) throws ProcessingMessageException {
        // RequestBody Check
        if (requestBody.isEmpty()) {
            throw new ProcessingMessageException("Error request with empty body",
                    "Error: request with empty body", 400);
        }
        return uncheckedCast(
                convertBody(requestBody.get(), clazz, "Error: request with empty body", 400, toTO));
    }

    private <TO extends MongodbTO<?>> Object convertBody(String body,
            Class<TO> clazz, String responseErrorMsg, Integer httpStatusCode, Boolean toTO)
            throws ProcessingMessageException {

        // TO deserialization
        TO receivedTO = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            receivedTO = objectMapper.readValue(body, clazz);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    responseErrorMsg, httpStatusCode);
        }

        // TO convertion and validation
        if (receivedTO == null) {
            throw new ProcessingMessageException(
                    "Error TO of type: " + clazz + " is null",
                    responseErrorMsg, httpStatusCode);
        } else {
            Object convertedObject = receivedTO.convertAndValidate();
            if (toTO) {
                return receivedTO;
            } else {
                return convertedObject;
            }

        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T uncheckedCast(Object objectToCast) {
        return (T) objectToCast;
    }

}