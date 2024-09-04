package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.MessageHandler;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

public abstract class AbstractBaseHandler<WFDTO extends AbstractWorkflowDTO<P>, P extends AbstractProduct>
        extends MessageHandler {

    protected final WorkflowDTORepository<WFDTO, ?> workflowRepository;

    public AbstractBaseHandler(List<TargetMessageHandler> bindings,
            WorkflowDTORepository<WFDTO, ?> workflowRepository) {
        super(bindings);
        this.workflowRepository = workflowRepository;
    }

    protected WFDTO getSubWorkflowIfRequested(CustomMessage context, String workflowId)
            throws ProcessingMessageException {
        WFDTO workflowDTO = null;

        Optional<String> subWorkflowId = getParamValue(context, ApiTemplateVariable.PARAM_KEY_SUBWORKFLOW);
        if (subWorkflowId.isPresent()) {
            Optional<WFDTO> topTierWorkflowDTO = workflowRepository.findWorkflowIfVertexExists(workflowId,
                    subWorkflowId.get());
            if (topTierWorkflowDTO.isEmpty()) {
                throw new ProcessingMessageException("SubWorkflow with subWorkflowId: " + subWorkflowId.get()
                        + " not found for Workflow with workflowId: " + workflowId, 404);
            }
            // Compute subworkflow
            AbstractWorkflow<P> subWorkflow = topTierWorkflowDTO.get().convertAndValidate()
                    .getProductWorkflow(subWorkflowId.get());

            if (subWorkflow == null) {
                throw new ProcessingMessageException(
                        "Product with productId: " + subWorkflowId.get() + " doesn't have a subworkflow", 400);
            }

            workflowDTO = uncheckedCast(topTierWorkflowDTO.get().buildFromWorkflow(subWorkflow));
        }

        return workflowDTO;
    }
}
