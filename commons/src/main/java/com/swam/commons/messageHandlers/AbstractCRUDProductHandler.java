package com.swam.commons.messageHandlers;

import java.util.List;
import java.util.Optional;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;

import com.qesm.workflow.AbstractProduct;
import com.qesm.workflow.AbstractWorkflow;
import com.swam.commons.intercommunication.ApiTemplateVariable;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.ProcessingMessageException;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.AbstractProductEntity;
import com.swam.commons.mongodb.AbstractWorkflowEntity;
import com.swam.commons.mongodb.WorkflowEntityRepository;

public abstract class AbstractCRUDProductHandler<WFE extends AbstractWorkflowEntity<P>, P extends AbstractProduct>
        extends AbstractCRUDHandler<WFE, P> {

    private final Class<? extends AbstractProductEntity<?>> clazzP;

    public AbstractCRUDProductHandler(
            WorkflowEntityRepository<WFE, ? extends AbstractProductEntity<P>> workflowRepository,
            Class<? extends AbstractProductEntity<P>> clazzP) {
        super(List.of(TargetMessageHandler.PRODUCT), workflowRepository);
        this.clazzP = clazzP;
    }

    @Override
    protected void get(CustomMessage context) throws ProcessingMessageException {
        String[] ids = getWorfklowAndProductIds(context);
        String workflowId = ids[0];
        String productId = ids[1];

        if (productId != null) {
            Optional<AbstractProductEntity<?>> receivedProduct = workflowRepository
                    .findVertexByWorkflowIdAndVertexName(workflowId, productId);
            if (receivedProduct.isEmpty()) {
                throw new ProcessingMessageException("Product with productId: " + productId + " not found", 404);
            } else {
                context.setResponse(receivedProduct.get(), 200);
            }
        } else {
            context.setResponse(
                    workflowRepository.findVertexMapProjectionByWorkflowId(workflowId).get().getVertexMap().values(),
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
        String[] ids = getWorfklowAndProductIds(context);
        String workflowId = ids[0];
        String productId = ids[1];

        AbstractProductEntity<?> abstractProductEntity = convertRequestBody(context.getRequestBody(),
                clazzP, true);

        if (workflowRepository.existVertexAndIsProcessed(workflowId, productId)) {
            Integer quantityProduced = abstractProductEntity.getQuantityProduced();
            StochasticTime pdf = abstractProductEntity.getPdf();
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
        String[] ids = getWorfklowAndProductIds(context);
        String workflowId = ids[0];
        String productId = ids[1];

        Optional<WFE> workflowEntity = workflowRepository
                .findWorkflowIfVertexExists(workflowId, productId);
        if (workflowEntity.isEmpty()) {
            throw new ProcessingMessageException("Product with productId: " + productId + " not found", 404);
        }

        AbstractWorkflow<P> workflow = workflowEntity.get().convertAndValidate();
        Optional<P> vertexToRemove = workflow.findProduct(productId);

        // Should never happen
        if (vertexToRemove.isEmpty()) {
            throw new ProcessingMessageException("Vertex to remove not found", "Internal server error", 500);
        }

        if (workflow.removeVertex(vertexToRemove.get())) {
            WFE updatedWorkflowEntity = uncheckedCast(workflowEntity.get().buildFromWorkflow(workflow));
            updatedWorkflowEntity.setId(workflowEntity.get().getId());
            workflowRepository.save(updatedWorkflowEntity);
            context.setResponse("Product with productId: " + productId + " correctly removed", 200);

        } else {
            throw new ProcessingMessageException("Cannot remove rootVertex, delete workflow instead", 400);
        }

    }

    private String[] getWorfklowAndProductIds(CustomMessage context) throws ProcessingMessageException {
        String workflowId = getUriId(context, ApiTemplateVariable.WORKFLOW_ID, true);
        String productId = getUriId(context, ApiTemplateVariable.PRODUCT_ID, false);

        if (!workflowRepository.existsById(workflowId)) {
            throw new ProcessingMessageException("Workflow with workflowId: " + workflowId + " not found", 404);
        }

        return new String[] { workflowId, productId };
    }
}
