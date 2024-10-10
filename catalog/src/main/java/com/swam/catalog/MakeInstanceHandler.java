package com.swam.catalog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.workflow.ProductTemplate;
import com.qesm.workflow.WorkflowInstance;
import com.qesm.workflow.WorkflowTemplate;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.messageHandlers.AbstractBaseHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntityRepository;

@Service
public class MakeInstanceHandler extends AbstractBaseHandler<WorkflowTemplateEntity, ProductTemplate> {

    public MakeInstanceHandler(WorkflowTemplateEntityRepository workflowTemplateEntityRepository) {
        super(List.of(TargetMessageHandler.ISTANCE_TEMPLATE), workflowTemplateEntityRepository);
    }

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) throws ProcessingMessageException {

        // DEBUG
        if (context.getRequestBody().isPresent()) {
            System.out.println("RequestBody: " + context.getRequestBody().get());
        }
        if (context.getRequestParams().isPresent()) {
            System.out.println("RequestParam: " + context.getRequestParams().get());
        }

        switch (triggeredBinding) {
            case TargetMessageHandler.ISTANCE_TEMPLATE:
                System.out.println("ISTANCE TEMPLATE");
                instanceTemplate(context);
                break;
            default:
                // TODO: handle error (maybe internal server error)
                // context.setError();
                break;
        }

    }

    private void instanceTemplate(CustomMessage context) throws ProcessingMessageException {
        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);

        // Check if workflow exist
        if (!workflowRepository.existsById(workflowId)) {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }

        // Check if it's requested a subworkflow instead of full workflow
        WorkflowTemplateEntity workflowEntity = getSubWorkflowIfRequested(context, workflowId);
        if (workflowEntity == null) {
            workflowEntity = workflowRepository.findById(workflowId).get();
        }

        // TODO: do we need additional sanity checks?
        WorkflowTemplate workflowTemplate = (WorkflowTemplate) workflowEntity.convertAndValidate();
        WorkflowInstance workflowInstance = workflowTemplate.makeInstance();
        WorkflowInstanceEntity workflowInstanceEntity = new WorkflowInstanceEntity(workflowInstance);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedWorkflowInstanceEntity = objectMapper.writeValueAsString(workflowInstanceEntity);
            context.setResponseBody(serializedWorkflowInstanceEntity);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    "Internal Server Error", 500);
        }

    }

}
