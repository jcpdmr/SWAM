package com.swam.commons;

import java.util.HashMap;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;

import lombok.Getter;

@Document
@Getter
public abstract class AbstractWorkflowDTO<P extends AbstractProductDTO, E extends AbstractCustomEdgeDTO> {

    protected @Id String id;
    protected final Set<P> vertexSet;
    protected final Set<E> edgeSet;
    protected final @Transient HashMap<String, ? extends AbstractProduct> nameToProductMap;

    public AbstractWorkflowDTO(Set<P> vertexSet,
            Set<E> edgeSet) {
        this.vertexSet = vertexSet;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    public abstract <V extends AbstractProduct, W extends AbstractWorkflow<V, W>> AbstractWorkflow<V, W> toWorkflow();

    @Override
    public String toString() {
        return vertexSet.toString() + " " + edgeSet.toString();
    }
}
