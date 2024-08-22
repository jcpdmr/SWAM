package com.swam.commons.mongodb;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
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
public abstract class AbstractWorkflowDTO<P extends AbstractProduct> {

    protected @Id String id;
    protected final Set<? extends AbstractProductDTO<P>> vertexSet;
    protected final Set<? extends AbstractCustomEdgeDTO> edgeSet;
    protected final List<AbstractWorkflowDTO<P>> subWorkflowDTOList;
    @JsonIgnore
    @Transient
    protected final HashMap<String, P> idToProductMap;

    protected AbstractWorkflowDTO(String id, Set<? extends AbstractProductDTO<P>> vertexSet,
            Set<? extends AbstractCustomEdgeDTO> edgeSet, List<AbstractWorkflowDTO<P>> subWorkflowDTOList) {
        this.id = id;
        this.vertexSet = vertexSet;
        this.edgeSet = edgeSet;
        this.subWorkflowDTOList = subWorkflowDTOList;
        this.idToProductMap = new HashMap<>();
    }

    protected AbstractWorkflowDTO(AbstractWorkflow<P> workflow) {

        this.vertexSet = workflow.getDag().vertexSet().stream().map(vertex -> createProductDTO(vertex))
                .collect(Collectors.toSet());
        this.edgeSet = workflow.getDag().edgeSet().stream().map(edge -> createCustomEdgeDTO(edge))
                .collect(Collectors.toSet());

        // Create subWorkflowDTOList only for the top tier workflowDTO (to avoid
        // recursive repetition of data)
        if (workflow.isTopTier()) {
            this.subWorkflowDTOList = new ArrayList<>();
            for (Entry<P, AbstractWorkflow<P>> entry : workflow.getProductToSubWorkflowMap().entrySet()) {
                subWorkflowDTOList.add(
                        createWorkflowDTO(entry.getValue(), entry.getKey().getUuid().toString()));
            }
        } else {
            this.subWorkflowDTOList = null;
        }

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

    protected abstract AbstractWorkflowDTO<P> createWorkflowDTO(AbstractWorkflow<P> workflow, String id);
}
