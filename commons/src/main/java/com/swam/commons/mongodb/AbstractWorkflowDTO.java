package com.swam.commons.mongodb;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;
import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractWorkflowDTO<P extends AbstractProduct> implements MongodbDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected @Id String id;
    protected final Map<String, ? extends AbstractProductDTO<P>> vertexMap;
    protected final Set<? extends AbstractCustomEdgeDTO> edgeSet;
    @JsonIgnore
    @Transient
    protected final HashMap<String, P> nameToProductMap;

    protected AbstractWorkflowDTO(String id, Map<String, ? extends AbstractProductDTO<P>> vertexMap,
            Set<? extends AbstractCustomEdgeDTO> edgeSet) {
        this.id = id;
        this.vertexMap = vertexMap;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    protected AbstractWorkflowDTO(Map<String, ? extends AbstractProductDTO<P>> vertexMap,
            Set<? extends AbstractCustomEdgeDTO> edgeSet) {
        this.id = null;
        this.vertexMap = vertexMap;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    protected AbstractWorkflowDTO(AbstractWorkflow<P> workflow) {
        this.id = null;
        this.vertexMap = workflow.getDag().vertexSet().stream()
                .collect(Collectors.toMap(vertex -> vertex.getName(), vertex -> createProductDTO(vertex)));
        this.edgeSet = workflow.getDag().edgeSet().stream().map(edge -> createCustomEdgeDTO(edge))
                .collect(Collectors.toSet());

        this.nameToProductMap = new HashMap<>();
    }

    public AbstractWorkflow<P> toWorkflow() {
        ListenableDAG<P, CustomEdge> dag = new ListenableDAG<>(CustomEdge.class);

        for (Entry<String, ? extends AbstractProductDTO<P>> producDTOEntry : vertexMap.entrySet()) {
            P product = producDTOEntry.getValue().toProduct();
            nameToProductMap.put(producDTOEntry.getKey(), product);
            dag.addVertex(product);
        }

        for (AbstractCustomEdgeDTO customEdgeDTO : edgeSet) {
            CustomEdge customEdge = dag.addEdge(nameToProductMap.get(customEdgeDTO.getSourceName()),
                    nameToProductMap.get(customEdgeDTO.getTargetName()));
            customEdge.setQuantityRequired(customEdgeDTO.getQuantityRequired());
        }

        return createWorkflow(dag);
    }

    @Override
    public Boolean isValid() {
        try {
            AbstractWorkflow<P> abstractWorkflow = toWorkflow();
            if (!abstractWorkflow.isDagConnected()) {
                System.err.println("Validation error: Workflow is not connected");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Validation error: cannot convert DTO to Workflow");
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    protected abstract AbstractProductDTO<P> createProductDTO(P vertex);

    protected abstract AbstractCustomEdgeDTO createCustomEdgeDTO(CustomEdge edge);

    protected abstract AbstractWorkflow<P> createWorkflow(ListenableDAG<P, CustomEdge> dag);
}
