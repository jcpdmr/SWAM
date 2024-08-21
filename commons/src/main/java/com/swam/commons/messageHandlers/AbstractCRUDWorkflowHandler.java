package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swam.commons.ApiTemplateVariables;
import com.swam.commons.CustomMessage;
import com.swam.commons.MessageDispatcher.MessageHandler;
import com.swam.commons.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCRUDWorkflowHandler<R extends WorkflowRepository<W>, W extends AbstractWorkflowDTO<?, ?>>
        implements MessageHandler {

    private final R workflowRepository;

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

        if (workflowId != null) {
            Optional<W> workflowDTO = workflowRepository.findById(workflowId);
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

        System.out.println(context.getRequestBody());
        ObjectMapper objectMapper = new ObjectMapper();

    }

    private void putWorkflow(CustomMessage context) {
    }

    private void deleteWorkflow(CustomMessage context) {
    }

}
