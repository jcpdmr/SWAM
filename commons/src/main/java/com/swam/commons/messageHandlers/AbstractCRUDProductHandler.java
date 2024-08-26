package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import com.qesm.AbstractProduct;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractProductDTO;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractCRUDProductHandler<WFDTO extends AbstractWorkflowDTO<? extends AbstractProduct>>
        extends AbstractCRUDHandler {

    private final WorkflowDTORepository<WFDTO, ?> workflowRepository;
    private final Class<WFDTO> clazz;

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.PRODUCT);
    }

    @Override
    protected void get(CustomMessage context) throws ProcessingMessageException {
        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);
        String productId = getUriId(context, ApiTemplateVariable.PRODUCT_ID, false);

        if (!workflowRepository.existsById(workflowId)) {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }

        if (productId != null) {
            Optional<AbstractProductDTO<?>> receivedProduct = workflowRepository
                    .findVertexByWorkflowIdAndVertexName(workflowId, productId);
            if (receivedProduct.isEmpty()) {
                throw new ProcessingMessageException("Product with productId: " + productId + " not found", 404);
            } else {
                context.setResponse(receivedProduct.get(), 200);
            }
        } else {
            // context.setResponse(workflowRepository.findAllVertexByWorkflowId(workflowId),
            // 200);
            context.setResponse(workflowRepository.findVertexMapProjectionByWorkflowId(workflowId).get().getVertexMap(),
                    200);
        }

    }

    @Override
    protected void post(CustomMessage context) throws ProcessingMessageException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'post'");
    }

    @Override
    protected void put(CustomMessage context) throws ProcessingMessageException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    protected void delete(CustomMessage context) throws ProcessingMessageException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
