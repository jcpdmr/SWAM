package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import com.qesm.AbstractProduct;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractCRUDWorkflowHandler<WFDTO extends AbstractWorkflowDTO<? extends AbstractProduct>>
        extends AbstractCRUDHandler {

    private final WorkflowDTORepository<WFDTO> workflowRepository;
    private final Class<WFDTO> clazz;

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.WORKFLOW);
    }

    @Override
    protected void get(CustomMessage context) throws ProcessingMessageException {

        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, false);
        System.out.println("ID: " + workflowId);

        if (workflowId != null) {
            Optional<WFDTO> workflowDTO = workflowRepository.findById(workflowId);
            if (workflowDTO.isEmpty()) {
                throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
            } else {
                context.setResponseBody(workflowDTO);
            }
        } else {
            // TODO: implement paging and filtering opt
            context.setResponseBody(workflowRepository.findAll());
        }
    }

    @Override
    protected void post(CustomMessage context) throws ProcessingMessageException {

        WFDTO receivedWorkflowDTO = convertBodyWithValidation(context, clazz);

        // Saving workflow to mongoDB
        WFDTO savedWorkflowDTO = workflowRepository.save(receivedWorkflowDTO);
        context.setResponseBody("Workflow correctly saved with id: " + savedWorkflowDTO.getId());

        System.out.println("POSTed Workflow correctly");
    }

    @Override
    protected void put(CustomMessage context) throws ProcessingMessageException {

        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);

        if (workflowRepository.existsById(workflowId)) {

            WFDTO receivedWorkflowDTO = convertBodyWithValidation(context, clazz);

            WFDTO savedWorkflowDTO = workflowRepository.save(receivedWorkflowDTO);
            context.setResponseBody("Workflow correctly saved with id: " + savedWorkflowDTO.getId());

            System.out.println("POSTed Workflow correctly");

        } else {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }
    }

    @Override
    protected void delete(CustomMessage context) throws ProcessingMessageException {
    }

}
