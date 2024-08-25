package com.swam.commons.mongodb.istance;

import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomEdgeIstanceDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    @JsonCreator
    public CustomEdgeIstanceDTO(@JsonProperty("sourceName") String sourceName,
            @JsonProperty("targetName") String targetName, @JsonProperty("quantityRequired") Integer quantityRequired) {
        super(sourceName, targetName, quantityRequired);

    }

    public CustomEdgeIstanceDTO(CustomEdge customEdge) {
        super(customEdge);
    }

}