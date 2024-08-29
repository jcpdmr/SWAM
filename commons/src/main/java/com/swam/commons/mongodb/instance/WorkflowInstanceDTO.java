package com.swam.commons.mongodb.instance;

import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DirectedAcyclicGraph;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.AbstractWorkflow;
import com.qesm.CustomEdge;
import com.qesm.ProductInstance;
import com.qesm.WorkflowInstance;
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
public class WorkflowInstanceDTO extends AbstractWorkflowDTO<ProductInstance> {

    @PersistenceCreator
    public WorkflowInstanceDTO(String id, Map<String, ProductInstanceDTO> vertexMap,
            Set<CustomEdgeInstanceDTO> edgeSet) {
        super(id, vertexMap, edgeSet);
    }

    @JsonCreator
    public WorkflowInstanceDTO(@JsonProperty("vertexMap") Map<String, ProductInstanceDTO> vertexMap,
            @JsonProperty("edgeSet") Set<CustomEdgeInstanceDTO> edgeSet) {
        super(vertexMap, edgeSet);
    }

    public WorkflowInstanceDTO(WorkflowInstance workflowInstance) {
        super(workflowInstance);
    }

    @Override
    protected AbstractProductDTO<ProductInstance> createProductDTO(ProductInstance vertex) {
        return new ProductInstanceDTO(vertex);
    }

    @Override
    protected AbstractCustomEdgeDTO createCustomEdgeDTO(CustomEdge edge) {
        return new CustomEdgeInstanceDTO(edge);
    }

    @Override
    protected WorkflowInstance createWorkflow(DirectedAcyclicGraph<ProductInstance, CustomEdge> dag) {
        return new WorkflowInstance(dag);
    }

    @Override
    public AbstractWorkflowDTO<ProductInstance> buildFromWorkflow(AbstractWorkflow<ProductInstance> workflow) {
        return new WorkflowInstanceDTO((WorkflowInstance) workflow);
    }
}
