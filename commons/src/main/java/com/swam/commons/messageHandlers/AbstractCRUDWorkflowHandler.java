package com.swam.commons.messageHandlers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
import com.qesm.Renderer;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

public abstract class AbstractCRUDWorkflowHandler<WFDTO extends AbstractWorkflowDTO<? extends AbstractProduct>>
        extends AbstractCRUDHandler {

    private final WorkflowDTORepository<WFDTO, ?> workflowRepository;
    private final Class<WFDTO> workflowClazz;

    public AbstractCRUDWorkflowHandler(WorkflowDTORepository<WFDTO, ?> workflowRepository, Class<WFDTO> workflowClazz) {
        super(List.of(TargetMessageHandler.WORKFLOW));
        this.workflowRepository = workflowRepository;
        this.workflowClazz = workflowClazz;
    }

    @Override
    protected void get(CustomMessage context) throws ProcessingMessageException {

        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, false);

        if (workflowId != null) {
            Optional<WFDTO> workflowDTO = workflowRepository.findById(workflowId);
            if (workflowDTO.isEmpty()) {
                throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
            } else {

                // System.out.println(context.getRequestParams());
                if (isParamSpecified(context, "format", "svg")) {
                    String dotFile;
                    AbstractWorkflow<?> workflow = workflowDTO.get().convertAndValidate();
                    try {
                        dotFile = workflow.exportDotFileNoSerialization().toString(StandardCharsets.UTF_8.name());
                    } catch (Exception e) {
                        throw new ProcessingMessageException(e.getMessage(), "Internal server error", 500);
                    }

                    ByteArrayOutputStream outputStream = Renderer
                            .renderDotFile(dotFile);

                    context.setResponse(outputStream.toString(), 200);
                } else {
                    context.setResponse(workflowDTO.get(), 200);
                }
            }
        } else {
            // TODO: implement paging and filtering opt
            context.setResponseBody(workflowRepository.findAll());
        }
    }

    @Override
    protected void post(CustomMessage context) throws ProcessingMessageException {

        WFDTO receivedWorkflowDTO = convertRequestBody(context.getRequestBody(), workflowClazz, true);

        // Saving workflow to mongoDB
        WFDTO savedWorkflowDTO = workflowRepository.save(receivedWorkflowDTO);
        context.setResponse("Workflow correctly saved with id: " + savedWorkflowDTO.getId(), 201);
    }

    @Override
    protected void put(CustomMessage context) throws ProcessingMessageException {

        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);

        if (workflowRepository.existsById(workflowId)) {

            WFDTO receivedWorkflowDTO = convertRequestBody(context.getRequestBody(), workflowClazz, true);
            receivedWorkflowDTO.setId(workflowId);

            WFDTO savedWorkflowDTO = workflowRepository.save(receivedWorkflowDTO);
            context.setResponse("Workflow with id: " + savedWorkflowDTO.getId() + " updated correctly", 200);

        } else {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }
    }

    @Override
    protected void delete(CustomMessage context) throws ProcessingMessageException {
        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);

        if (workflowRepository.existsById(workflowId)) {

            workflowRepository.deleteById(workflowId);
            context.setResponse("Workflow with workflowId: " + workflowId + " deleted correctly", 200);

        } else {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }
    }
}
