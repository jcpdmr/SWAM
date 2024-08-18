package com.swam.catalog;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.swam.commons.ApiTemplateVariables;
import com.swam.commons.CustomMessage;
import com.swam.commons.WorkflowTypeDTO;
import com.swam.commons.WorkflowTypeRepository;
import com.swam.commons.MessageHandler.MethodExecutor;
import com.swam.commons.OrchestratorInfo.TargetMethods;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetWorkflow implements MethodExecutor {

    private final WorkflowTypeRepository workflowTypeRepository;

    @Override
    public void execute(CustomMessage context) {

        Map<String, String> uriTemplateVariables = context.getUriTemplateVariables();
        String workflowId = uriTemplateVariables.get(ApiTemplateVariables.WORKFLOW_ID);

        if (workflowId != null) {
            Optional<WorkflowTypeDTO> workflowTypeDTO = workflowTypeRepository.findById(workflowId);
            if (workflowTypeDTO.isEmpty()) {
                context.setResponseStatusCode(404);
                context.setResponseBody("Workflow with workflowId: " + workflowId + " not found");
            } else {
                context.setResponseBody(workflowTypeDTO);
            }
        } else {
            // TODO: implement paging and filtering opt
            context.setResponseBody(workflowTypeRepository.findAll());
        }
    }

    @Override
    public TargetMethods getBinding() {
        return TargetMethods.GET_WORKFLOW;
    }

}
