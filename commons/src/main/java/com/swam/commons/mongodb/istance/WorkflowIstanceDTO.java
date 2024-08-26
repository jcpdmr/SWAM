package com.swam.commons.mongodb.istance;

import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;
import com.qesm.ProductIstance;

import com.qesm.WorkflowIstance;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;
import com.swam.commons.mongodb.AbstractProductDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@ToString(callSuper = true)
public class WorkflowIstanceDTO extends AbstractWorkflowDTO<ProductIstance> {

    @PersistenceCreator
    public WorkflowIstanceDTO(String id, Map<String, ProductIstanceDTO> vertexMap,
            Set<CustomEdgeIstanceDTO> edgeSet) {
        super(id, vertexMap, edgeSet);
    }

    @JsonCreator
    public WorkflowIstanceDTO(@JsonProperty("vertexMap") Map<String, ProductIstanceDTO> vertexMap,
            @JsonProperty("edgeSet") Set<CustomEdgeIstanceDTO> edgeSet) {
        super(vertexMap, edgeSet);
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

}
