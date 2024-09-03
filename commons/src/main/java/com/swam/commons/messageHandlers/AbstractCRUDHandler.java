package com.swam.commons.messageHandlers;

import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;

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

    protected abstract void get(CustomMessage context) throws ProcessingMessageException;

    protected abstract void post(CustomMessage context) throws ProcessingMessageException;

    protected abstract void put(CustomMessage context) throws ProcessingMessageException;

    protected abstract void delete(CustomMessage context) throws ProcessingMessageException;

}
