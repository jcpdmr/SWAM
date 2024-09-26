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
public abstract class AbstractWorkflowTO<P extends AbstractProduct> implements MongodbTO<AbstractWorkflow<P>> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected @Id String id;
    protected final Map<String, ? extends AbstractProductTO<P>> vertexMap;
    protected final Set<? extends AbstractCustomEdgeTO> edgeSet;
    @JsonIgnore
    @Transient
    protected final HashMap<String, P> nameToProductMap;

    protected AbstractWorkflowTO(String id, Map<String, ? extends AbstractProductTO<P>> vertexMap,
            Set<? extends AbstractCustomEdgeTO> edgeSet) {
        this.id = id;
        this.vertexMap = vertexMap;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    protected AbstractWorkflowTO(Map<String, ? extends AbstractProductTO<P>> vertexMap,
            Set<? extends AbstractCustomEdgeTO> edgeSet) {
        this.id = null;
        this.vertexMap = vertexMap;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    protected AbstractWorkflowTO(AbstractWorkflow<P> workflow) {
        this.id = null;
        this.vertexMap = workflow.cloneDag().vertexSet().stream()
                .collect(Collectors.toMap(vertex -> vertex.getName(), vertex -> createProductTO(vertex)));
        this.edgeSet = workflow.cloneDag().edgeSet().stream().map(edge -> createCustomEdgeTO(edge))
                .collect(Collectors.toSet());

        this.nameToProductMap = new HashMap<>();
    }

    private AbstractWorkflow<P> toWorkflow() throws ProcessingMessageException {
        DirectedAcyclicGraph<P, CustomEdge> dag = new DirectedAcyclicGraph<>(CustomEdge.class);

        for (Entry<String, ? extends AbstractProductTO<P>> producTOEntry : vertexMap.entrySet()) {
            P product = producTOEntry.getValue().convertAndValidate();
            nameToProductMap.put(producTOEntry.getKey(), product);
            dag.addVertex(product);
        }

        for (AbstractCustomEdgeTO customEdgeTO : edgeSet) {
            CustomEdge customEdge = dag.addEdge(nameToProductMap.get(customEdgeTO.getSourceName()),
                    nameToProductMap.get(customEdgeTO.getTargetName()));
            customEdge.setQuantityRequired(customEdgeTO.getQuantityRequired());
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

    public AbstractWorkflowTO<P> getSubWorkflowTO(String vertexName) throws ProcessingMessageException {
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

        AbstractWorkflowTO<P> workflowTOToCompare = uncheckedCast(obj);

        if (!vertexMap.equals(workflowTOToCompare.vertexMap)) {
            return false;
        }
        if (!edgeSet.equals(workflowTOToCompare.edgeSet)) {
            return false;
        }

        return true;
    }

    public abstract AbstractWorkflowTO<P> buildFromWorkflow(AbstractWorkflow<P> workflow);

    protected abstract AbstractProductTO<P> createProductTO(P vertex);

    protected abstract AbstractCustomEdgeTO createCustomEdgeTO(CustomEdge edge);

    protected abstract AbstractWorkflow<P> createWorkflow(DirectedAcyclicGraph<P, CustomEdge> dag);
}
