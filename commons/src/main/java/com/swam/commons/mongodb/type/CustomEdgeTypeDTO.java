package com.swam.commons.mongodb.type;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;

import lombok.Getter;

@Document
@Getter
public class CustomEdgeTypeDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeTypeDTO(@JsonProperty("id") String id, @JsonProperty("sourceId") String sourceId,
            @JsonProperty("targetId") String targetId, @JsonProperty("quantityRequired") Integer quantityRequired) {
        super(id, sourceId, targetId, quantityRequired);

    }

    public CustomEdgeTypeDTO(CustomEdge customEdge) {
        super(customEdge);
    }

}
