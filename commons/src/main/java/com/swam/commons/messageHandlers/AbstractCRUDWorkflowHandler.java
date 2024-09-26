package com.swam.commons.messageHandlers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
import com.qesm.Renderer;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowTO;
import com.swam.commons.mongodb.WorkflowTORepository;

public abstract class AbstractCRUDWorkflowHandler<WFTO extends AbstractWorkflowTO<P>, P extends AbstractProduct>
        extends AbstractCRUDHandler<WFTO, P> {

    private final Class<WFTO> workflowClazz;

    public AbstractCRUDWorkflowHandler(WorkflowTORepository<WFTO, ?> workflowRepository, Class<WFTO> workflowClazz) {
        super(List.of(TargetMessageHandler.WORKFLOW), workflowRepository);
        this.workflowClazz = workflowClazz;
    }

    @Override
    protected void get(CustomMessage context) throws ProcessingMessageException {

        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, false);

        if (workflowId != null) {

            // Check if workflow exist
            if (!workflowRepository.existsById(workflowId)) {
                throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
            }

            // Check if it's requested a subworkflow instead of full workflow
            WFTO workflowTO = getSubWorkflowIfRequested(context, workflowId);
            if (workflowTO == null) {
                workflowTO = workflowRepository.findById(workflowId).get();
            }

            // Check if it's requested svg format or json data
            if (isParamSpecified(context, ApiTemplateVariable.PARAM_KEY_FORMAT,
                    ApiTemplateVariable.PARAM_FORMAT_SVG)) {
                String dotFile;
                AbstractWorkflow<?> workflow = workflowTO.convertAndValidate();
                try {
                    dotFile = workflow.exportDotFileNoSerialization().toString(StandardCharsets.UTF_8.name());
                } catch (Exception e) {
                    throw new ProcessingMessageException(e.getMessage(), "Internal server error", 500);
                }

                ByteArrayOutputStream outputStream = Renderer
                        .renderDotFile(dotFile);

                context.setResponse(outputStream.toString(), 200);
            } else {
                context.setResponse(workflowTO, 200);
            }

        } else {
            // TODO: implement paging and filtering opt
            context.setResponseBody(workflowRepository.findAll());
        }
    }

    @Override
    protected void post(CustomMessage context) throws ProcessingMessageException {

        WFTO receivedWorkflowTO = convertRequestBody(context.getRequestBody(), workflowClazz, true);

        // Saving workflow to mongoDB
        WFTO savedWorkflowTO = workflowRepository.save(receivedWorkflowTO);
        context.setResponse("Workflow correctly saved with id: " + savedWorkflowTO.getId(), 201);
    }

    @Override
    protected void put(CustomMessage context) throws ProcessingMessageException {

        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);

        if (workflowRepository.existsById(workflowId)) {

            WFTO receivedWorkflowTO = convertRequestBody(context.getRequestBody(), workflowClazz, true);
            receivedWorkflowTO.setId(workflowId);

            WFTO savedWorkflowTO = workflowRepository.save(receivedWorkflowTO);
            context.setResponse("Workflow with id: " + savedWorkflowTO.getId() + " updated correctly", 200);

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
