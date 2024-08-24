package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
import com.swam.commons.intercommunication.ApiTemplateVariables;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageDispatcher.MessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCRUDWorkflowHandler<WFDTO extends AbstractWorkflowDTO<? extends AbstractProduct>>
        implements MessageHandler {

    private final WorkflowDTORepository<WFDTO> workflowRepository;
    private final Class<WFDTO> clazz;

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) {
        System.out.println("Execute CRUD");
        switch (triggeredBinding) {
            case TargetMessageHandler.GET_WORKFLOW:
                System.out.println("GET");
                getWorkflow(context);
                break;
            case TargetMessageHandler.POST_WORKFLOW:
                System.out.println("POST");
                postWorkflow(context);
                break;
            case TargetMessageHandler.PUT_WORKFLOW:
                System.out.println("PUT");
                putWorkflow(context);
                break;
            case TargetMessageHandler.DELETE_WORKFLOW:
                System.out.println("DELETE");
                deleteWorkflow(context);
                break;
            default:
                // TODO: handle error (maybe internal server error)
                // context.setError();
                break;
        }
    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.GET_WORKFLOW, TargetMessageHandler.POST_WORKFLOW,
                TargetMessageHandler.PUT_WORKFLOW,
                TargetMessageHandler.DELETE_WORKFLOW);
    }

    private void getWorkflow(CustomMessage context) {
        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String workflowId = uriTemplateVariables.get(ApiTemplateVariables.WORKFLOW_ID);
        System.out.println("ID: " + workflowId);

        if (workflowId != null) {
            Optional<WFDTO> workflowDTO = workflowRepository.findById(workflowId);
            if (workflowDTO.isEmpty()) {
                context.setError("Workflow with workflowId: " + workflowId + " not found", 404);
            } else {
                context.setResponseBody(workflowDTO);
            }
        } else {
            // TODO: implement paging and filtering opt
            context.setResponseBody(workflowRepository.findAll());
        }
    }

    private void postWorkflow(CustomMessage context) {

        // RequestBody Check
        if (context.getRequestBody().isEmpty()) {
            System.out.println("POST request with empty body, skipped...");
            context.setError("POST request with empty body", 400);
            return;
        }

        // DTO deserialization
        WFDTO receivedWorkflowDTO = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            receivedWorkflowDTO = objectMapper.readValue(context.getRequestBody().get(), clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            context.setError("POST request with malformed body", 400);
            return;
        }

        // DTO validation and saving
        if (isDTOValid(receivedWorkflowDTO)) {
            WFDTO savedWorkflowDTO = workflowRepository.save(receivedWorkflowDTO);
            context.setResponseBody("Workflow correctly saved with id: " + savedWorkflowDTO.getId());

            System.out.println("POSTed Workflow correctly");
        } else {
            context.setError("POST request with malformed body", 400);
        }

    }

    private void putWorkflow(CustomMessage context) {
        if (context.getRequestBody().isEmpty()) {
            System.out.println("PUT request with empty body, skipped...");
            context.setError("PUT request with empty body", 400);
            return;
        }

        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String workflowId = uriTemplateVariables.get(ApiTemplateVariables.WORKFLOW_ID);
        System.out.println("ID: " + workflowId);

        Assert.notNull(workflowId, "Put with null workflowId");

        if (workflowRepository.existsById(workflowId)) {
            // DTO deserialization
            WFDTO receivedWorkflowDTO = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                receivedWorkflowDTO = objectMapper.readValue(context.getRequestBody().get(), clazz);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                context.setError("POST request with malformed body", 400);
                return;
            }

            // DTO validation and saving
            if (isDTOValid(receivedWorkflowDTO)) {
                WFDTO savedWorkflowDTO = workflowRepository.save(receivedWorkflowDTO);
                context.setResponseBody("Workflow correctly saved with id: " + savedWorkflowDTO.getId());

                System.out.println("POSTed Workflow correctly");
            } else {
                context.setError("POST request with malformed body", 400);
            }
        } else {

        }

    }

    private void deleteWorkflow(CustomMessage context) {
    }

    private Boolean isDTOValid(WFDTO workflowDTO) {
        if (workflowDTO == null) {
            System.out.println("Validation error: null DTO");
            return false;
        } else {
            try {
                AbstractWorkflow<?> abstractWorkflow = workflowDTO.toWorkflow();
                if (!abstractWorkflow.isDagConnected()) {
                    System.out.println("Validation error: Workflow is not connected");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Validation error: cannot convert DTO to Workflow");
                return false;
            }
            return true;
        }

    }

}
