package com.swam.commons.mongodb.type;

import java.util.Set;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.AbstractWorkflow;
import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;
import com.qesm.ProductType;
import com.qesm.WorkflowType;
import com.swam.commons.mongodb.AbstractBaseWorkflowDTO;

import lombok.Getter;
import lombok.Setter;

@Document("Workflow")
@Getter
@Setter
public class BaseWorkflowTypeDTO extends AbstractBaseWorkflowDTO<ProductType> {

    @PersistenceCreator
    @JsonCreator
    public BaseWorkflowTypeDTO(@JsonProperty("id") String id, @JsonProperty("vertexSet") Set<ProductTypeDTO> vertexSet,
            @JsonProperty("edgeSet") Set<CustomEdgeTypeDTO> edgeSet) {
        super(id, vertexSet, edgeSet);
    }

    public BaseWorkflowTypeDTO(WorkflowType workflowType) {
        super(workflowType);
    }

    @Override
    protected ProductTypeDTO createProductDTO(ProductType vertex) {
        return new ProductTypeDTO(vertex);
    }

    @Override
    protected CustomEdgeTypeDTO createCustomEdgeDTO(CustomEdge edge) {
        return new CustomEdgeTypeDTO(edge);
    }

    @Override
    protected WorkflowType createWorkflow(ListenableDAG<ProductType, CustomEdge> dag) {
        return new WorkflowType(dag);
    }

    @Override
    protected HeadWorkflowTypeDTO createWorkflowDTO(AbstractWorkflow<ProductType> workflow, String id) {
        HeadWorkflowTypeDTO workflowTypeDTO = new HeadWorkflowTypeDTO((WorkflowType) workflow);
        workflowTypeDTO.setId(id);
        return workflowTypeDTO;
    }

}
