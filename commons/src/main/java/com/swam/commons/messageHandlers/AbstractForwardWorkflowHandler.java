package com.swam.commons.messageHandlers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.AbstractProduct;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

public abstract class AbstractForwardWorkflowHandler<WFDTO extends AbstractWorkflowDTO<P>, P extends AbstractProduct>
        extends AbstractBaseHandler<WFDTO, P> {

    public AbstractForwardWorkflowHandler(WorkflowDTORepository<WFDTO, ?> workflowRepository) {
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
        WFDTO workflowDTO = getSubWorkflowIfRequested(context, workflowId);
        if (workflowDTO == null) {
            workflowDTO = workflowRepository.findById(workflowId).get();
        }

        // DTO serialization
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedWorkflowDTO = objectMapper.writeValueAsString(workflowDTO);
            context.setResponseBody(serializedWorkflowDTO);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    "Internal Server Error", 500);
        }

        logger.info("Workflow forwarded successfully");
    }

}
