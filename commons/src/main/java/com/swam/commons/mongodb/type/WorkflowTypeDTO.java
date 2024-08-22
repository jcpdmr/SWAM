package com.swam.commons.mongodb.type;

import java.util.List;
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
import com.swam.commons.mongodb.AbstractWorkflowDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document("Workflow")
@Getter
@Setter
@ToString
public class WorkflowTypeDTO extends AbstractWorkflowDTO<ProductType> {

    @PersistenceCreator
    @JsonCreator
    public WorkflowTypeDTO(@JsonProperty("id") String id, @JsonProperty("vertexSet") Set<ProductTypeDTO> vertexSet,
            @JsonProperty("edgeSet") Set<CustomEdgeTypeDTO> edgeSet,
            List<AbstractWorkflowDTO<ProductType>> subWorkflowDTOList) {
        super(id, vertexSet, edgeSet, subWorkflowDTOList);
    }

    public WorkflowTypeDTO(WorkflowType workflowType) {
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
    protected WorkflowTypeDTO createWorkflowDTO(AbstractWorkflow<ProductType> workflow, String id) {
        WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO((WorkflowType) workflow);
        workflowTypeDTO.setId(id);
        return workflowTypeDTO;
    }

}
