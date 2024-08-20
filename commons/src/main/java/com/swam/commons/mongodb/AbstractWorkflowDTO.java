package com.swam.commons.mongodb;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public abstract class AbstractWorkflowDTO<V extends AbstractProduct, W extends AbstractWorkflow<V>> {

    protected @Id String id;
    protected final Set<? extends AbstractProductDTO<V>> vertexSet;
    protected final Set<? extends AbstractCustomEdgeDTO> edgeSet;
    @JsonIgnore
    @Transient
    protected final HashMap<String, V> idToProductMap;

    protected AbstractWorkflowDTO(String id, Set<? extends AbstractProductDTO<V>> vertexSet,
            Set<? extends AbstractCustomEdgeDTO> edgeSet) {
        this.id = id;
        this.vertexSet = vertexSet;
        this.edgeSet = edgeSet;
        this.idToProductMap = new HashMap<>();
    }

    protected AbstractWorkflowDTO(AbstractWorkflow<V> workflow) {

        this.vertexSet = workflow.getDag().vertexSet().stream().map(vertex -> createProductDTO(vertex))
                .collect(Collectors.toSet());
        this.edgeSet = workflow.getDag().edgeSet().stream().map(edge -> createCustomEdgeDTO(edge))
                .collect(Collectors.toSet());
        this.idToProductMap = new HashMap<>();
    }

    public W toWorkflow() {
        ListenableDAG<V, CustomEdge> dag = new ListenableDAG<>(CustomEdge.class);

        for (AbstractProductDTO<V> producDTO : vertexSet) {
            V product = producDTO.toProduct();
            idToProductMap.put(producDTO.getId(), product);
            dag.addVertex(product);
        }

        for (AbstractCustomEdgeDTO customEdgeDTO : edgeSet) {
            CustomEdge customEdge = dag.addEdge(idToProductMap.get(customEdgeDTO.getSourceId()),
                    idToProductMap.get(customEdgeDTO.getTargetId()));
            customEdge.setQuantityRequired(customEdgeDTO.getQuantityRequired());
        }

        return createWorkflow(dag);
    }

    protected abstract AbstractProductDTO<V> createProductDTO(V vertex);

    protected abstract AbstractCustomEdgeDTO createCustomEdgeDTO(CustomEdge edge);

    protected abstract W createWorkflow(ListenableDAG<V, CustomEdge> dag);
}
