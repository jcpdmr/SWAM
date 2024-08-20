package com.swam.commons.mongodb.istance;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;

import lombok.Getter;

@Document
@Getter
public class CustomEdgeIstanceDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeIstanceDTO(String id, String sourceId, String targetId, Integer quantityRequired) {
        super(id, sourceId, targetId, quantityRequired);

    }

    public CustomEdgeIstanceDTO(CustomEdge customEdge) {
        super(customEdge);
    }

}