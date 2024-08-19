package com.swam.commons;

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
    protected final @Transient HashMap<String, T> nameToProductMap;

    public AbstractWorkflowDTO(Set<P> vertexSet,
            Set<E> edgeSet) {
        this.vertexSet = vertexSet;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    public abstract AbstractWorkflow<T, ? extends AbstractWorkflow<T, ?>> toWorkflow();

    @Override
    public String toString() {
        return vertexSet.toString() + " " + edgeSet.toString();
    }
}
