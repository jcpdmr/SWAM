package com.swam.catalog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qesm.ProductTemplate;
import com.qesm.WorkflowInstance;
import com.qesm.WorkflowTemplate;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.messageHandlers.AbstractBaseHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceTO;
import com.swam.commons.mongodb.template.WorkflowTemplateTO;
import com.swam.commons.mongodb.template.WorkflowTemplateTORepository;

@Service
public class MakeInstanceHandler extends AbstractBaseHandler<WorkflowTemplateTO, ProductTemplate> {

    public MakeInstanceHandler(WorkflowTemplateTORepository workflowTemplateTORepository) {
        super(List.of(TargetMessageHandler.ISTANCE_TEMPLATE), workflowTemplateTORepository);
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
        WorkflowTemplateTO workflowTO = getSubWorkflowIfRequested(context, workflowId);
        if (workflowTO == null) {
            workflowTO = workflowRepository.findById(workflowId).get();
        }

        // TODO: do we need additional sanity checks?
        WorkflowTemplate workflowTemplate = (WorkflowTemplate) workflowTO.convertAndValidate();
        WorkflowInstance workflowInstance = workflowTemplate.makeInstance();
        WorkflowInstanceTO workflowInstanceTO = new WorkflowInstanceTO(workflowInstance);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedWorkflowInstanceTO = objectMapper.writeValueAsString(workflowInstanceTO);
            context.setResponseBody(serializedWorkflowInstanceTO);
        } catch (JsonProcessingException e) {
            throw new ProcessingMessageException(e.getMessage(),
                    "Internal Server Error", 500);
        }

    }

}
