package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.AbstractProduct;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractForwardWorkflowHandler<WFDTO extends AbstractWorkflowDTO<? extends AbstractProduct>>
        implements MessageHandler {

    private final WorkflowDTORepository<WFDTO, ?> workflowRepository;

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.FORWARD_WORKFLOW);
    }

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {
        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);
        System.out.println("ID: " + workflowId);

        Optional<WFDTO> workflowDTO = workflowRepository.findById(workflowId);
        if (workflowDTO.isEmpty()) {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedWorkflowDTO = objectMapper.writeValueAsString(workflowDTO.get());
                context.setResponseBody(serializedWorkflowDTO);
            } catch (JsonProcessingException e) {
                throw new ProcessingMessageException(e.getMessage(),
                        "Internal Server Error", 500);
            }
        }
    }

}
