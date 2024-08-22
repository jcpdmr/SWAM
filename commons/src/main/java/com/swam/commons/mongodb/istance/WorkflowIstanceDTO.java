package com.swam.commons.mongodb.istance;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.AbstractWorkflow;
import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;
import com.qesm.ProductIstance;

import com.qesm.WorkflowIstance;

import com.swam.commons.mongodb.AbstractCustomEdgeDTO;
import com.swam.commons.mongodb.AbstractProductDTO;
import com.swam.commons.mongodb.AbstractWorkflowDTO;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
public class WorkflowIstanceDTO extends AbstractWorkflowDTO<ProductIstance> {

    @PersistenceCreator
    public WorkflowIstanceDTO(String id, Set<ProductIstanceDTO> vertexSet,
            Set<CustomEdgeIstanceDTO> edgeSet,
            List<AbstractWorkflowDTO<ProductIstance>> subWorkflowDTOList) {
        super(id, vertexSet, edgeSet, subWorkflowDTOList);
    }

    public WorkflowIstanceDTO(WorkflowIstance workflowIstance) {
        super(workflowIstance);
    }

    @Override
    protected AbstractProductDTO<ProductIstance> createProductDTO(ProductIstance vertex) {
        return new ProductIstanceDTO(vertex);
    }

    @Override
    protected AbstractCustomEdgeDTO createCustomEdgeDTO(CustomEdge edge) {
        return new CustomEdgeIstanceDTO(edge);
    }

    @Override
    protected WorkflowIstance createWorkflow(ListenableDAG<ProductIstance, CustomEdge> dag) {
        return new WorkflowIstance(dag);
    }

    @Override
    protected WorkflowIstanceDTO createWorkflowDTO(AbstractWorkflow<ProductIstance> workflow, String id) {
        WorkflowIstanceDTO workflowIstanceDTO = new WorkflowIstanceDTO((WorkflowIstance) workflow);
        workflowIstanceDTO.setId(id);
        return workflowIstanceDTO;
    }

}
