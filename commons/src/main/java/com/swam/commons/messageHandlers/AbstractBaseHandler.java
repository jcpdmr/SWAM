package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import com.qesm.workflow.AbstractProduct;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowEntity;
import com.swam.commons.mongodb.WorkflowEntityRepository;

public abstract class AbstractBaseHandler<WFE extends AbstractWorkflowEntity<P>, P extends AbstractProduct>
        extends MessageHandler {

    protected final WorkflowEntityRepository<WFE, ?> workflowRepository;

    public AbstractBaseHandler(List<TargetMessageHandler> bindings,
            WorkflowEntityRepository<WFE, ?> workflowRepository) {
        super(bindings);
        this.workflowRepository = workflowRepository;
    }

    protected WFE getSubWorkflowIfRequested(CustomMessage context, String workflowId)
            throws ProcessingMessageException {
        WFE workflowEntity = null;

        Optional<String> subWorkflowId = getParamValue(context, ApiTemplateVariable.PARAM_KEY_SUBWORKFLOW);
        if (subWorkflowId.isPresent()) {
            Optional<WFE> topTierWorkflowEntity = workflowRepository.findWorkflowIfVertexExists(workflowId,
                    subWorkflowId.get());
            if (topTierWorkflowEntity.isEmpty()) {
                throw new ProcessingMessageException("SubWorkflow with subWorkflowId: " + subWorkflowId.get()
                        + " not found for Workflow with workflowId: " + workflowId, 404);
            }
            // Compute subworkflow
            workflowEntity = uncheckedCast(topTierWorkflowEntity.get().getSubWorkflowEntity(subWorkflowId.get()));
        }

        return workflowEntity;
    }
}
