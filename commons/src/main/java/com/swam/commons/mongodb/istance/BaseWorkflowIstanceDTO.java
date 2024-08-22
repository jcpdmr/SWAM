package com.swam.commons.mongodb.istance;

import java.util.Set;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.AbstractWorkflow;
import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;
import com.qesm.ProductIstance;

import com.qesm.WorkflowIstance;
import com.swam.commons.mongodb.AbstractBaseWorkflowDTO;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;
import com.swam.commons.mongodb.AbstractProductDTO;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
public class BaseWorkflowIstanceDTO extends AbstractBaseWorkflowDTO<ProductIstance> {

    @PersistenceCreator
    public BaseWorkflowIstanceDTO(String id, Set<ProductIstanceDTO> vertexSet,
            Set<CustomEdgeIstanceDTO> edgeSet) {
        super(id, vertexSet, edgeSet);
    }

    public BaseWorkflowIstanceDTO(WorkflowIstance workflowIstance) {
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
    protected HeadWorkflowIstanceDTO createWorkflowDTO(AbstractWorkflow<ProductIstance> workflow, String id) {
        HeadWorkflowIstanceDTO workflowIstanceDTO = new HeadWorkflowIstanceDTO((WorkflowIstance) workflow);
        workflowIstanceDTO.setId(id);
        return workflowIstanceDTO;
    }

}
