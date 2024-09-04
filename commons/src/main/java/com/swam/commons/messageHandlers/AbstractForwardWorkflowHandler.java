package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

public abstract class AbstractForwardWorkflowHandler<WFDTO extends AbstractWorkflowDTO<P>, P extends AbstractProduct>
        extends MessageHandler {

    private final WorkflowDTORepository<WFDTO, ?> workflowRepository;

    public AbstractForwardWorkflowHandler(WorkflowDTORepository<WFDTO, ?> workflowRepository) {
        super(List.of(TargetMessageHandler.FORWARD_WORKFLOW));
        this.workflowRepository = workflowRepository;
    }

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);

        // Check if workflow exist
        if (!workflowRepository.existsById(workflowId)) {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }

        WFDTO workflowDTO;

        Optional<String> subWorkflowId = getParamValue(context, ApiTemplateVariable.PARAM_KEY_SUBWORKFLOW);
        if (subWorkflowId.isPresent()) {
            Optional<WFDTO> topTierWorkflowDTO = workflowRepository.findWorkflowIfVertexExists(workflowId,
                    subWorkflowId.get());
            if (topTierWorkflowDTO.isEmpty()) {
                throw new ProcessingMessageException("SubWorkflow with subWorkflowId: " + subWorkflowId
                        + " not found for Workflow with workflowId: " + workflowId, 404);
            }
            // Compute subworkflow
            AbstractWorkflow<P> subWorkflow = topTierWorkflowDTO.get().convertAndValidate()
                    .getProductWorkflow(subWorkflowId.get());

            if (subWorkflow == null) {
                throw new ProcessingMessageException(
                        "Product with productId: " + subWorkflowId.get() + " doesn't have a subworkflow", 400);
            }

            workflowDTO = uncheckedCast(topTierWorkflowDTO.get().buildFromWorkflow(subWorkflow));
        } else {
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
