package com.swam.commons.mongodb.istance;

import org.springframework.data.annotation.PersistenceCreator;

import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomEdgeIstanceDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeIstanceDTO(String sourceName, String targetName, Integer quantityRequired) {
        super(sourceName, targetName, quantityRequired);

    }

    public CustomEdgeIstanceDTO(CustomEdge customEdge) {
        super(customEdge);
    }

}