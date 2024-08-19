package com.swam.commons.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;

import lombok.Getter;

@Document
@Getter
public abstract class AbstractCustomEdgeDTO {

    private @Id String id;
    protected final String sourceId;
    protected final String targetId;
    protected final Integer quantityRequired;

    protected AbstractCustomEdgeDTO(String sourceId, String targetId,
            Integer quantityRequired) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.quantityRequired = quantityRequired;
    }

    public abstract CustomEdge toEdge();

    @Override
    public String toString() {
        return "( " + sourceId + " -> " + targetId + " )";
    }

}
