package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.AbstractProduct;
import com.swam.commons.intercommunication.ApiTemplateVariables;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageDispatcher.MessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractHeadWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCRUDWorkflowHandler<WFDTO extends AbstractHeadWorkflowDTO<? extends AbstractProduct>>
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

        // System.out.println(context.getRequestBody());
        if (context.getRequestBody().isEmpty()) {
            System.out.println("POST request with empty body, skipped...");
            context.setError("POST request with empty body", 400);
            return;
        }

        WFDTO receivedWorkflowDTO = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            receivedWorkflowDTO = objectMapper.readValue(context.getRequestBody().get(), clazz);
        } catch (JsonProcessingException e) {
            // TODO: need to handle problems in JSON to Object conversion
            e.printStackTrace();

        }

        if (receivedWorkflowDTO == null) {
            System.out.println("Problems with JSON to Object conversion");
            return;
        }

        workflowRepository.save(receivedWorkflowDTO);
        System.out.println("POSTed Workflow correctly");

        context.setResponseStatusCode(200);

    }

    private void putWorkflow(CustomMessage context) {
    }

    private void deleteWorkflow(CustomMessage context) {
    }

}
