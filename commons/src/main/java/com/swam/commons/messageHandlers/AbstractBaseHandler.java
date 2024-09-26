package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import com.qesm.AbstractProduct;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowTO;
import com.swam.commons.mongodb.WorkflowTORepository;

public abstract class AbstractBaseHandler<WFTO extends AbstractWorkflowTO<P>, P extends AbstractProduct>
        extends MessageHandler {

    protected final WorkflowTORepository<WFTO, ?> workflowRepository;

    public AbstractBaseHandler(List<TargetMessageHandler> bindings,
            WorkflowTORepository<WFTO, ?> workflowRepository) {
        super(bindings);
        this.workflowRepository = workflowRepository;
    }

    protected WFTO getSubWorkflowIfRequested(CustomMessage context, String workflowId)
            throws ProcessingMessageException {
        WFTO workflowTO = null;

        Optional<String> subWorkflowId = getParamValue(context, ApiTemplateVariable.PARAM_KEY_SUBWORKFLOW);
        if (subWorkflowId.isPresent()) {
            Optional<WFTO> topTierWorkflowTO = workflowRepository.findWorkflowIfVertexExists(workflowId,
                    subWorkflowId.get());
            if (topTierWorkflowTO.isEmpty()) {
                throw new ProcessingMessageException("SubWorkflow with subWorkflowId: " + subWorkflowId.get()
                        + " not found for Workflow with workflowId: " + workflowId, 404);
            }
            // Compute subworkflow
            workflowTO = uncheckedCast(topTierWorkflowTO.get().getSubWorkflowTO(subWorkflowId.get()));
        }

        return workflowTO;
    }
}
