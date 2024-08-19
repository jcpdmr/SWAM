package com.swam.commons.task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.qesm.AbstractProduct;
import com.swam.commons.AbstractCustomEdgeDTO;
import com.swam.commons.AbstractProductDTO;
import com.swam.commons.AbstractWorkflowDTO;
import com.swam.commons.ApiTemplateVariables;
import com.swam.commons.CustomMessage;
import com.swam.commons.WorkflowRepository;
import com.swam.commons.MessageDispatcher.TaskExecutor;
import com.swam.commons.RoutingInstructions.TargetTasks;

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
                getWorkflow(context);
                break;
            case TargetTasks.POST_WORKFLOW:
                postWorkflow(context);
                break;
            case TargetTasks.PUT_WORKFLOW:
                putWorkflow(context);
                break;
            case TargetTasks.DELETE_WORKFLOW:
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
        System.out.println("post");
    }

    private void putWorkflow(CustomMessage context) {
    }

    private void deleteWorkflow(CustomMessage context) {
    }

}
