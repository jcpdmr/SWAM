package com.swam.commons.messageHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageDispatcher.MessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.MongodbDTO;

import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

public abstract class AbstractCRUDHandler implements MessageHandler {

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) {
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
            context.setError("Internal Server error", 500);
        }

    }

    protected <DTO extends MongodbDTO> DTO convertBodyWithValidation(CustomMessage context,
            Class<DTO> clazz) throws Exception {
        // RequestBody Check
        if (context.getRequestBody().isEmpty()) {
            context.setError(context.getRequestMethod() + " request with empty body", 400);
            throw new RuntimeException("Error request with empty body");
        }

        // DTO deserialization
        DTO receivedDTO = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            receivedDTO = objectMapper.readValue(context.getRequestBody().get(), clazz);
        } catch (JsonProcessingException e) {
            context.setError(context.getRequestMethod() + " request with malformed body", 400);
            throw e;
        }

        // DTO validation
        if (receivedDTO != null && receivedDTO.isValid()) {
            return receivedDTO;
        } else {
            context.setError(context.getRequestMethod() + " request with malformed body", 400);
            throw new RuntimeException("Error DTO of type: " + clazz + " is null or can't be validated properly");
        }
    }

    protected String getUriId(CustomMessage context, ApiTemplateVariable apiTemplateVariable, Boolean isRequired) {
        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String uriId = uriTemplateVariables.get(apiTemplateVariable.value());
        if (isRequired) {
            Assert.notNull(uriId, context.getRequestMethod() + " with null " + apiTemplateVariable);
        }
        return uriId;

    }

    protected abstract void get(CustomMessage context);

    protected abstract void post(CustomMessage context);

    protected abstract void put(CustomMessage context);

    protected abstract void delete(CustomMessage context);

}
