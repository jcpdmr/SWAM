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
import com.swam.commons.mongodb.AbstractWorkflowTO;
import com.swam.commons.mongodb.AbstractCustomEdgeTO;
import com.swam.commons.mongodb.AbstractProductTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document
@Getter
@Setter
@ToString(callSuper = true)
public class WorkflowInstanceTO extends AbstractWorkflowTO<ProductInstance> {

    @PersistenceCreator
    public WorkflowInstanceTO(String id, Map<String, ProductInstanceTO> vertexMap,
            Set<CustomEdgeInstanceTO> edgeSet) {
        super(id, vertexMap, edgeSet);
    }

    @JsonCreator
    public WorkflowInstanceTO(@JsonProperty("vertexMap") Map<String, ProductInstanceTO> vertexMap,
            @JsonProperty("edgeSet") Set<CustomEdgeInstanceTO> edgeSet) {
        super(vertexMap, edgeSet);
    }

    public WorkflowInstanceTO(WorkflowInstance workflowInstance) {
        super(workflowInstance);
    }

    @Override
    protected AbstractProductTO<ProductInstance> createProductTO(ProductInstance vertex) {
        return new ProductInstanceTO(vertex);
    }

    @Override
    protected AbstractCustomEdgeTO createCustomEdgeTO(CustomEdge edge) {
        return new CustomEdgeInstanceTO(edge);
    }

    @Override
    protected WorkflowInstance createWorkflow(DirectedAcyclicGraph<ProductInstance, CustomEdge> dag) {
        return new WorkflowInstance(dag);
    }

    @Override
    public AbstractWorkflowTO<ProductInstance> buildFromWorkflow(AbstractWorkflow<ProductInstance> workflow) {
        return new WorkflowInstanceTO((WorkflowInstance) workflow);
    }
}
