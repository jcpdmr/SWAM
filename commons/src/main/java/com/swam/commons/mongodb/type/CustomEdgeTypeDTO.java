package com.swam.commons.mongodb.type;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;

import lombok.Getter;

@Document
@Getter
public class CustomEdgeTypeDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeTypeDTO(String id, String sourceId, String targetId, Integer quantityRequired) {
        super(id, sourceId, targetId, quantityRequired);

    }

    public CustomEdgeTypeDTO(CustomEdge customEdge) {
        super(customEdge);
    }

}
