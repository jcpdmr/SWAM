package com.swam.commons.mongodb.instance;

import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DirectedAcyclicGraph;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.workflow.AbstractWorkflow;
import com.qesm.workflow.CustomEdge;
import com.qesm.workflow.ProductInstance;
import com.qesm.workflow.WorkflowInstance;
import com.swam.commons.mongodb.AbstractWorkflowEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@ToString(callSuper = true)
public class WorkflowInstanceEntity extends AbstractWorkflowEntity<ProductInstance> {

    @PersistenceCreator
    public WorkflowInstanceEntity(String id, Map<String, ProductInstanceEntity> vertexMap,
            Set<CustomEdgeInstanceEntity> edgeSet) {
        super(id, vertexMap, edgeSet);
    }

    @JsonCreator
    public WorkflowInstanceEntity(@JsonProperty("vertexMap") Map<String, ProductInstanceEntity> vertexMap,
            @JsonProperty("edgeSet") Set<CustomEdgeInstanceEntity> edgeSet) {
        super(vertexMap, edgeSet);
    }

    public WorkflowInstanceEntity(WorkflowInstance workflowInstance) {
        super(workflowInstance);
    }

    @Override
    protected ProductInstanceEntity createProductEntity(ProductInstance vertex) {
        return new ProductInstanceEntity(vertex);
    }

    @Override
    protected CustomEdgeInstanceEntity createCustomEdgeEntity(CustomEdge edge) {
        return new CustomEdgeInstanceEntity(edge);
    }

    @Override
    protected WorkflowInstance createWorkflow(DirectedAcyclicGraph<ProductInstance, CustomEdge> dag) {
        return new WorkflowInstance(dag);
    }

    @Override
    public AbstractWorkflowEntity<ProductInstance> buildFromWorkflow(AbstractWorkflow<ProductInstance> workflow) {
        return new WorkflowInstanceEntity((WorkflowInstance) workflow);
    }
}
