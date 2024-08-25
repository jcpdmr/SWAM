package com.swam.commons.messageHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.MessageDispatcher.MessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.MongodbDTO;

import java.util.Map;

import org.springframework.http.HttpMethod;

public abstract class AbstractCRUDHandler implements MessageHandler {

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        System.out.println("Execute CRUD");

        if (context.getRequestMethod().equals(HttpMethod.GET)) {
            System.out.println("GET");
            get(context);
        } else if (context.getRequestMethod().equals(HttpMethod.POST)) {
            System.out.println("POST");
            post(context);
        } else if (context.getRequestMethod().equals(HttpMethod.PUT)) {
            System.out.println("PUT");
            put(context);
        } else if (context.getRequestMethod().equals(HttpMethod.DELETE)) {
            System.out.println("DELETE");
            delete(context);
        } else {
            throw new ProcessingMessageException("Error requestMethod: " + context.getRequestMethod() + " not handled",
                    "Internal Server error", 500);
        }

    }

    protected <DTO extends MongodbDTO> DTO convertBodyWithValidation(CustomMessage context,
            Class<DTO> clazz) throws ProcessingMessageException {
        // RequestBody Check
        if (context.getRequestBody().isEmpty()) {
            throw new ProcessingMessageException("Error request with empty body",
                    context.getRequestMethod() + " request with empty body", 400);
        }

        // DTO deserialization
        DTO receivedDTO = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            receivedDTO = objectMapper.readValue(context.getRequestBody().get(), clazz);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    context.getRequestMethod() + " request with empty body", 400);
        }

        // DTO validation
        if (receivedDTO != null && receivedDTO.isValid()) {
            return receivedDTO;
        } else {
            throw new ProcessingMessageException(
                    "Error DTO of type: " + clazz + " is null or can't be validated properly",
                    context.getRequestMethod() + " request with empty or not valid body", 400);
        }
    }

    protected String getUriId(CustomMessage context, ApiTemplateVariable apiTemplateVariable, Boolean isRequired)
            throws ProcessingMessageException {
        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String uriId = uriTemplateVariables.get(apiTemplateVariable.value());
        if (isRequired && uriId == null) {
            throw new ProcessingMessageException(context.getRequestMethod() + " with null " + apiTemplateVariable,
                    "Internal server error", 500);
        }
        return uriId;

    }

    protected abstract void get(CustomMessage context) throws ProcessingMessageException;

    protected abstract void post(CustomMessage context) throws ProcessingMessageException;

    protected abstract void put(CustomMessage context) throws ProcessingMessageException;

    protected abstract void delete(CustomMessage context) throws ProcessingMessageException;

}
