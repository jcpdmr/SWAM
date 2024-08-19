package com.swam.commons.mongodb;

import java.util.HashMap;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;

import lombok.Getter;

@Getter
public abstract class AbstractWorkflowDTO<P extends AbstractProductDTO, E extends AbstractCustomEdgeDTO, T extends AbstractProduct> {

    protected @Id String id;
    protected final Set<P> vertexSet;
    protected final Set<E> edgeSet;
    @JsonIgnore
    @Transient
    protected final HashMap<String, T> idToProductMap;

    public AbstractWorkflowDTO(Set<P> vertexSet,
            Set<E> edgeSet) {
        this.vertexSet = vertexSet;
        this.edgeSet = edgeSet;
        this.idToProductMap = new HashMap<>();
    }

    public abstract AbstractWorkflow<T, ? extends AbstractWorkflow<T, ?>> toWorkflow();

    @Override
    public String toString() {
        return vertexSet.toString() + " " + edgeSet.toString();
    }

    // public AbstractWorkflowDTO<P, E, T> newIstance(Set<P> vertexSet, Set<E>
    // edgeSet) {
    // return new AbstractWorkflowDTO<P, E, T>(vertexSet, edgeSet);
    // }
}
