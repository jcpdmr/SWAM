package com.swam.commons.mongodb.type;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;
import com.qesm.ProductType;
import com.qesm.WorkflowType;
import com.swam.commons.mongodb.AbstractWorkflowDTO;

import lombok.Getter;
import lombok.Setter;

@Document("Workflow")
@Getter
@Setter
public class WorkflowTypeDTO extends AbstractWorkflowDTO<ProductType, WorkflowType> {

    @PersistenceCreator
    public WorkflowTypeDTO(String id, Set<ProductTypeDTO> vertexSet,
            Set<CustomEdgeTypeDTO> edgeSet,
            List<AbstractWorkflowDTO<ProductType, WorkflowType>> subWorkflowDTOList) {
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
    protected AbstractWorkflowDTO<ProductType, WorkflowType> createWorkflowDTO(WorkflowType workflow, String id) {
        WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO(workflow);
        workflowTypeDTO.setId(id);
        return workflowTypeDTO;
    }

}
