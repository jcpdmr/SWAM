package com.swam.commons.mongodb;

import com.qesm.AbstractProduct;
import com.qesm.CustomEdge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public abstract class AbstractCustomEdgeDTO {

    protected final String sourceName;
    protected final String targetName;
    protected final Integer quantityRequired;

    protected AbstractCustomEdgeDTO(CustomEdge customEdge) {
        this.sourceName = ((AbstractProduct) customEdge.getSource()).getName();
        this.targetName = ((AbstractProduct) customEdge.getTarget()).getName();
        this.quantityRequired = customEdge.getQuantityRequired();
    }

}
