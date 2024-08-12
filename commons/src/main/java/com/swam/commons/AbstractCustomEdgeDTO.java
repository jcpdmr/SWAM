package com.swam.commons;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;

import lombok.Getter;

@Document
@Getter
public abstract class AbstractCustomEdgeDTO {

    private @Id String id;
    protected final AbstractProductDTO source;
    protected final AbstractProductDTO target;
    protected final Integer quantityRequired;

    protected AbstractCustomEdgeDTO(AbstractProductDTO source, AbstractProductDTO target,
            Integer quantityRequired) {
        this.source = source;
        this.target = target;
        this.quantityRequired = quantityRequired;
    }

    public abstract CustomEdge toEdge();

    @Override
    public String toString() {
        return "( " + source + " -> " + target + " )";
    }

}
