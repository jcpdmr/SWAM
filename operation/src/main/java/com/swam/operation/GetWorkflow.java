package com.swam.operation;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.swam.commons.ApiTemplateVariables;
import com.swam.commons.CustomMessage;
import com.swam.commons.WorkflowIstanceDTO;
import com.swam.commons.WorkflowIstanceRepository;
import com.swam.commons.MessageHandler.TaskExecutor;
import com.swam.commons.OrchestratorInfo.TargetTasks;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetWorkflow implements TaskExecutor {

    private final WorkflowIstanceRepository workflowIstanceRepository;

    @Override
    public void execute(CustomMessage context) {

        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String workflowId = uriTemplateVariables.get(ApiTemplateVariables.WORKFLOW_ID);

        if (workflowId != null) {
            Optional<WorkflowIstanceDTO> workflowIstanceDTO = workflowIstanceRepository.findById(workflowId);
            if (workflowIstanceDTO.isEmpty()) {
                context.setResponseStatusCode(404);
                context.setResponseBody("Workflow with workflowId: " + workflowId + " not found");
            } else {
                context.setResponseBody(workflowIstanceDTO);
            }
        } else {
            // TODO: implement paging and filtering opt
            context.setResponseBody(workflowIstanceRepository.findAll());
        }

    }

    @Override
    public TargetTasks getBinding() {
        return TargetTasks.GET_WORKFLOW;
    }

}
