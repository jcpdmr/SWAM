package com.swam.commons.messageHandlers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.workflow.AbstractProduct;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowEntity;
import com.swam.commons.mongodb.WorkflowEntityRepository;

public abstract class AbstractForwardWorkflowHandler<WFE extends AbstractWorkflowEntity<P>, P extends AbstractProduct>
        extends AbstractBaseHandler<WFE, P> {

    public AbstractForwardWorkflowHandler(WorkflowEntityRepository<WFE, ?> workflowRepository) {
        super(List.of(TargetMessageHandler.FORWARD_WORKFLOW), workflowRepository);
    }

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);

        // Check if workflow exist
        if (!workflowRepository.existsById(workflowId)) {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }

        // Check if it's requested a subworkflow analysis instead of full workflow
        // analysis
        WFE workflowEntity = getSubWorkflowIfRequested(context, workflowId);
        if (workflowEntity == null) {
            workflowEntity = workflowRepository.findById(workflowId).get();
        }

        // Entity serialization
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedWorkflowEntity = objectMapper.writeValueAsString(workflowEntity);
            context.setResponseBody(serializedWorkflowEntity);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    "Internal Server Error", 500);
        }

        logger.info("Workflow forwarded successfully");
    }

}
