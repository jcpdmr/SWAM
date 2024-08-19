package com.swam.commons.task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.AbstractProduct;
import com.swam.commons.ApiTemplateVariables;
import com.swam.commons.CustomMessage;
import com.swam.commons.MessageDispatcher.TaskExecutor;
import com.swam.commons.RoutingInstructions.TargetTasks;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;
import com.swam.commons.mongodb.AbstractProductDTO;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCRUDWorkflowTask<P extends AbstractProductDTO, E extends AbstractCustomEdgeDTO, T extends AbstractProduct, D extends AbstractWorkflowDTO<P, E, T>, R extends WorkflowRepository<D>>
        implements TaskExecutor {

    private final R workflowRepository;

    @Override
    public void execute(CustomMessage context, TargetTasks triggeredBinding) {
        System.out.println("Execute CRUD");
        switch (triggeredBinding) {
            case TargetTasks.GET_WORKFLOW:
                System.out.println("GET");
                getWorkflow(context);
                break;
            case TargetTasks.POST_WORKFLOW:
                System.out.println("POST");
                postWorkflow(context);
                break;
            case TargetTasks.PUT_WORKFLOW:
                System.out.println("PUT");
                putWorkflow(context);
                break;
            case TargetTasks.DELETE_WORKFLOW:
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
    public List<TargetTasks> getBinding() {
        return List.of(TargetTasks.GET_WORKFLOW, TargetTasks.POST_WORKFLOW, TargetTasks.PUT_WORKFLOW,
                TargetTasks.DELETE_WORKFLOW);
    }

    private void getWorkflow(CustomMessage context) {
        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String workflowId = uriTemplateVariables.get(ApiTemplateVariables.WORKFLOW_ID);

        if (workflowId != null) {
            Optional<D> workflowDTO = workflowRepository.findById(workflowId);
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
