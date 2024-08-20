package com.swam.commons.mongodb;

import org.springframework.data.annotation.Id;

import com.qesm.AbstractProduct;
import com.qesm.CustomEdge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public abstract class AbstractCustomEdgeDTO {

    private @Id String id;
    protected final String sourceId;
    protected final String targetId;
    protected final Integer quantityRequired;

    protected AbstractCustomEdgeDTO(CustomEdge customEdge) {
        this.sourceId = ((AbstractProduct) customEdge.getSource()).getUuid().toString();
        this.targetId = ((AbstractProduct) customEdge.getTarget()).getUuid().toString();
        this.quantityRequired = customEdge.getQuantityRequired();
    }

}
