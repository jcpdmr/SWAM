package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;

import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
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

    private final WorkflowDTORepository<WFDTO, ? extends AbstractProductDTO<? extends AbstractProduct>> workflowRepository;
    private final Class<? extends AbstractProductDTO<?>> clazzP;

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.PRODUCT);
    }

    @Override
    protected void get(CustomMessage context) throws ProcessingMessageException {
        List<String> ids = getWorfklowAndProductIds(context);
        String workflowId = ids.get(0);
        String productId = ids.get(1);

        if (productId != null) {
            Optional<AbstractProductDTO<?>> receivedProduct = workflowRepository
                    .findVertexByWorkflowIdAndVertexName(workflowId, productId);
            if (receivedProduct.isEmpty()) {
                throw new ProcessingMessageException("Product with productId: " + productId + " not found", 404);
            } else {
                context.setResponse(receivedProduct.get(), 200);
            }
        } else {
            context.setResponse(workflowRepository.findVertexMapProjectionByWorkflowId(workflowId).get().getVertexMap(),
                    200);
        }

    }

    @Override
    protected void post(CustomMessage context) throws ProcessingMessageException {
        throw new ProcessingMessageException(
                "Post method not allowed for product, should have been intercepted by Dispatcher",
                "Internal server error", 500);
    }

    @Override
    protected void put(CustomMessage context) throws ProcessingMessageException {
        List<String> ids = getWorfklowAndProductIds(context);
        String workflowId = ids.get(0);
        String productId = ids.get(1);

        AbstractProductDTO<?> abstractProductDTO = convertBodyWithValidation(context, clazzP);

        if (workflowRepository.existVertexAndIsProcessed(workflowId, productId)) {
            Integer quantityProduced = abstractProductDTO.getQuantityProduced();
            StochasticTime pdf = abstractProductDTO.getPdf();
            Integer updatedCount = 0;
            if (quantityProduced != null && pdf != null) {
                updatedCount = workflowRepository.updateVertexQuantityProducedAndPdf(workflowId, productId,
                        quantityProduced, pdf);
            } else if (quantityProduced != null) {
                updatedCount = workflowRepository.updateVertexQuantityProduced(workflowId, productId, quantityProduced);
            } else if (pdf != null) {
                updatedCount = workflowRepository.updateVertexPdf(workflowId, productId, pdf);
            } else {
                throw new ProcessingMessageException("Cannot update product if both pdf and quantityProduced are null",
                        400);
            }

            if (updatedCount == 1) {
                context.setResponse("Product updated correctly", 200);
            } else {
                throw new ProcessingMessageException("Cannot update product",
                        500);
            }

        } else {
            throw new ProcessingMessageException(
                    "Product with productId: " + productId + " not found or it's not PROCESSED type", 404);
        }

    }

    @Override
    protected void delete(CustomMessage context) throws ProcessingMessageException {
        List<String> ids = getWorfklowAndProductIds(context);
        String workflowId = ids.get(0);
        String productId = ids.get(1);

        Optional<AbstractWorkflowDTO<AbstractProduct>> workflowDTO = workflowRepository
                .findWorkflowIfVertexExists(workflowId, productId);
        if (workflowDTO.isEmpty()) {
            throw new ProcessingMessageException("Product with productId: " + productId + " not found", 404);
        }

        AbstractWorkflow<AbstractProduct> workflow = workflowDTO.get().convertAndValidate();
        Optional<AbstractProduct> vertexToRemove = workflow.findProduct(productId);

        // Should never happen
        if (vertexToRemove.isEmpty()) {
            throw new ProcessingMessageException("Vertex to remove not found", "Internal server error", 500);
        }

        if (workflow.removeVertex(vertexToRemove.get())) {
            WFDTO updatedWorkflowDTO = uncheckedCast(workflowDTO.get().buildFromWorkflow(workflow));
            updatedWorkflowDTO.setId(workflowDTO.get().getId());
            workflowRepository.save(updatedWorkflowDTO);
            context.setResponse("Product with productId: " + productId + " correctly removed", 200);

        } else {
            throw new ProcessingMessageException("Cannot remove rootVertex, delete workflow instead", 400);
        }

    }

    private List<String> getWorfklowAndProductIds(CustomMessage context) throws ProcessingMessageException {
        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);
        String productId = getUriId(context, ApiTemplateVariable.PRODUCT_ID, false);

        if (!workflowRepository.existsById(workflowId)) {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }

        return List.of(workflowId, productId);
    }

    @SuppressWarnings("unchecked")
    private <T> T uncheckedCast(Object objToCast) {
        return (T) objToCast;
    }

}