package com.swam.commons.mongodb.type;

import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomEdgeTypeDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeTypeDTO(@JsonProperty("sourceName") String sourceName,
            @JsonProperty("targetName") String targetName, @JsonProperty("quantityRequired") Integer quantityRequired) {
        super(sourceName, targetName, quantityRequired);

    }

    public CustomEdgeTypeDTO(CustomEdge customEdge) {
        super(customEdge);
    }

}
