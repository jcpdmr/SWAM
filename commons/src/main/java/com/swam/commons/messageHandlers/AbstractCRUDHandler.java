package com.swam.commons.messageHandlers;

import com.qesm.workflow.AbstractProduct;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowEntity;
import com.swam.commons.mongodb.WorkflowEntityRepository;

import java.util.List;

import org.springframework.http.HttpMethod;

public abstract class AbstractCRUDHandler<WFE extends AbstractWorkflowEntity<P>, P extends AbstractProduct>
        extends AbstractBaseHandler<WFE, P> {

    public AbstractCRUDHandler(List<TargetMessageHandler> bindings,
            WorkflowEntityRepository<WFE, ?> workflowRepository) {
        super(bindings, workflowRepository);
    }

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        // System.out.println("Execute CRUD");

        if (context.getRequestMethod().equals(HttpMethod.GET)) {
            get(context);
            logger.info("GET Workflow correctly");
        } else if (context.getRequestMethod().equals(HttpMethod.POST)) {
            post(context);
            logger.info("POST Workflow correctly");
        } else if (context.getRequestMethod().equals(HttpMethod.PUT)) {
            put(context);
            logger.info("PUT Workflow correctly");
        } else if (context.getRequestMethod().equals(HttpMethod.DELETE)) {
            delete(context);
            logger.info("DELETE Workflow correctly");
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
