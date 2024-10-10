package com.swam.commons.messageHandlers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.qesm.workflow.AbstractProduct;
import com.qesm.workflow.AbstractWorkflow;
import com.qesm.io.Renderer;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowEntity;
import com.swam.commons.mongodb.WorkflowEntityRepository;

public abstract class AbstractCRUDWorkflowHandler<WFE extends AbstractWorkflowEntity<P>, P extends AbstractProduct>
        extends AbstractCRUDHandler<WFE, P> {

    private final Class<WFE> workflowClazz;

    public AbstractCRUDWorkflowHandler(WorkflowEntityRepository<WFE, ?> workflowRepository,
            Class<WFE> workflowClazz) {
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
            WFE workflowEntity = getSubWorkflowIfRequested(context, workflowId);
            if (workflowEntity == null) {
                workflowEntity = workflowRepository.findById(workflowId).get();
            }

            // Check if it's requested svg format or json data
            if (isParamSpecified(context, ApiTemplateVariable.PARAM_KEY_FORMAT,
                    ApiTemplateVariable.PARAM_FORMAT_SVG)) {
                String dotFile;
                AbstractWorkflow<?> workflow = workflowEntity.convertAndValidate();
                try {
                    dotFile = workflow.exportDotFileNoSerialization().toString(StandardCharsets.UTF_8.name());
                } catch (Exception e) {
                    throw new ProcessingMessageException(e.getMessage(), "Internal server error", 500);
                }

                ByteArrayOutputStream outputStream = Renderer
                        .renderDotFile(dotFile);

                context.setResponse(outputStream.toString(), 200);
            } else {
                context.setResponse(workflowEntity, 200);
            }

        } else {
            // TODO: implement paging and filtering opt
            context.setResponseBody(workflowRepository.findAll());
        }
    }

    @Override
    protected void post(CustomMessage context) throws ProcessingMessageException {

        WFE receivedWorkflowEntity = convertRequestBody(context.getRequestBody(), workflowClazz, true);

        // Saving workflow to mongoDB
        WFE savedWorkflowEntity = workflowRepository.save(receivedWorkflowEntity);
        context.setResponse("Workflow correctly saved with id: " + savedWorkflowEntity.getId(), 201);
    }

    @Override
    protected void put(CustomMessage context) throws ProcessingMessageException {

        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);

        if (workflowRepository.existsById(workflowId)) {

            WFE receivedWorkflowEntity = convertRequestBody(context.getRequestBody(), workflowClazz, true);
            receivedWorkflowEntity.setId(workflowId);

            WFE savedWorkflowEntity = workflowRepository.save(receivedWorkflowEntity);
            context.setResponse("Workflow with id: " + savedWorkflowEntity.getId() + " updated correctly", 200);

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
