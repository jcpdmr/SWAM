package com.swam.commons.mongodb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.GraphCycleProhibitedException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.workflow.AbstractProduct;
import com.qesm.workflow.AbstractWorkflow;
import com.qesm.workflow.CustomEdge;
import com.qesm.workflow.WorkflowValidationException;
import com.swam.commons.intercommunication.ProcessingMessageException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractWorkflowEntity<P extends AbstractProduct> implements MongodbEntity<AbstractWorkflow<P>> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected @Id String id;
    protected final Map<String, ? extends AbstractProductEntity<P>> vertexMap;
    protected final Set<? extends AbstractCustomEdgeEntity> edgeSet;
    @JsonIgnore
    @Transient
    protected final HashMap<String, P> nameToProductMap;

    protected AbstractWorkflowEntity(String id, Map<String, ? extends AbstractProductEntity<P>> vertexMap,
            Set<? extends AbstractCustomEdgeEntity> edgeSet) {
        this.id = id;
        this.vertexMap = vertexMap;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    protected AbstractWorkflowEntity(Map<String, ? extends AbstractProductEntity<P>> vertexMap,
            Set<? extends AbstractCustomEdgeEntity> edgeSet) {
        this.id = null;
        this.vertexMap = vertexMap;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    protected AbstractWorkflowEntity(AbstractWorkflow<P> workflow) {
        this.id = null;
        this.vertexMap = workflow.cloneDag().vertexSet().stream()
                .collect(Collectors.toMap(vertex -> vertex.getName(), vertex -> createProductEntity(vertex)));
        this.edgeSet = workflow.cloneDag().edgeSet().stream().map(edge -> createCustomEdgeEntity(edge))
                .collect(Collectors.toSet());

        this.nameToProductMap = new HashMap<>();
    }

    private AbstractWorkflow<P> toWorkflow() throws ProcessingMessageException {
        DirectedAcyclicGraph<P, CustomEdge> dag = new DirectedAcyclicGraph<>(CustomEdge.class);

        for (Entry<String, ? extends AbstractProductEntity<P>> productEntityEntry : vertexMap.entrySet()) {
            P product = productEntityEntry.getValue().convertAndValidate();
            nameToProductMap.put(productEntityEntry.getKey(), product);
            dag.addVertex(product);
        }

        for (AbstractCustomEdgeEntity customEdgeEntity : edgeSet) {
            CustomEdge customEdge = dag.addEdge(nameToProductMap.get(customEdgeEntity.getSourceName()),
                    nameToProductMap.get(customEdgeEntity.getTargetName()));
            customEdge.setQuantityRequired(customEdgeEntity.getQuantityRequired());
        }

        return createWorkflow(dag);
    }

    @Override
    public AbstractWorkflow<P> convertAndValidate() throws ProcessingMessageException {
        try {
            return toWorkflow();
        } catch (WorkflowValidationException e) {
            throw new ProcessingMessageException(e.getMessage(), 400);
        } catch (GraphCycleProhibitedException e) {
            throw new ProcessingMessageException("Validation error: there is a cycle", 400);
        } catch (Exception e) {
            throw new ProcessingMessageException(e.getMessage(), "Internal server Error", 500);
        }
    }

    public AbstractWorkflowEntity<P> getSubWorkflowEntity(String vertexName) throws ProcessingMessageException {
        if (!vertexMap.containsKey(vertexName)) {
            throw new ProcessingMessageException("SubWorkflow with subWorkflowId: " + vertexName
                    + " not found for Workflow with workflowId: " + id, 404);
        }

        AbstractWorkflow<P> subWorkflow = this.convertAndValidate().getProductWorkflow(vertexName);
        if (subWorkflow == null) {
            throw new ProcessingMessageException(
                    "Product with productId: " + vertexName + " doesn't have a subworkflow", 400);
        }
        return buildFromWorkflow(subWorkflow);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        AbstractWorkflowEntity<P> workflowEntityToCompare = uncheckedCast(obj);

        if (!vertexMap.equals(workflowEntityToCompare.vertexMap)) {
            return false;
        }
        if (!edgeSet.equals(workflowEntityToCompare.edgeSet)) {
            return false;
        }

        return true;
    }

    public abstract AbstractWorkflowEntity<P> buildFromWorkflow(AbstractWorkflow<P> workflow);

    protected abstract AbstractProductEntity<P> createProductEntity(P vertex);

    protected abstract AbstractCustomEdgeEntity createCustomEdgeEntity(CustomEdge edge);

    protected abstract AbstractWorkflow<P> createWorkflow(DirectedAcyclicGraph<P, CustomEdge> dag);
}
