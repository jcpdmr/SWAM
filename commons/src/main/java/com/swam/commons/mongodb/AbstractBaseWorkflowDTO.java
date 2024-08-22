package com.swam.commons.mongodb;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public abstract class AbstractBaseWorkflowDTO<P extends AbstractProduct> {

    protected @Id String id;
    protected final Set<? extends AbstractProductDTO<P>> vertexSet;
    protected final Set<? extends AbstractCustomEdgeDTO> edgeSet;
    @JsonIgnore
    @Transient
    protected final HashMap<String, P> idToProductMap;

    protected AbstractBaseWorkflowDTO(String id, Set<? extends AbstractProductDTO<P>> vertexSet,
            Set<? extends AbstractCustomEdgeDTO> edgeSet) {
        this.id = id;
        this.vertexSet = vertexSet;
        this.edgeSet = edgeSet;
        this.idToProductMap = new HashMap<>();
    }

    protected AbstractBaseWorkflowDTO(AbstractWorkflow<P> workflow) {

        this.vertexSet = workflow.getDag().vertexSet().stream().map(vertex -> createProductDTO(vertex))
                .collect(Collectors.toSet());
        this.edgeSet = workflow.getDag().edgeSet().stream().map(edge -> createCustomEdgeDTO(edge))
                .collect(Collectors.toSet());

        this.idToProductMap = new HashMap<>();
    }

    public AbstractWorkflow<P> toWorkflow() {
        ListenableDAG<P, CustomEdge> dag = new ListenableDAG<>(CustomEdge.class);

        for (AbstractProductDTO<P> producDTO : vertexSet) {
            P product = producDTO.toProduct();
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

    protected abstract AbstractProductDTO<P> createProductDTO(P vertex);

    protected abstract AbstractCustomEdgeDTO createCustomEdgeDTO(CustomEdge edge);

    protected abstract AbstractWorkflow<P> createWorkflow(ListenableDAG<P, CustomEdge> dag);

    protected abstract AbstractBaseWorkflowDTO<P> createWorkflowDTO(AbstractWorkflow<P> workflow, String id);
}
