package com.swam.commons.mongodb.instance;

import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CustomEdgeInstanceDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    @JsonCreator
    public CustomEdgeInstanceDTO(@JsonProperty("sourceName") String sourceName,
            @JsonProperty("targetName") String targetName, @JsonProperty("quantityRequired") Integer quantityRequired) {
        super(sourceName, targetName, quantityRequired);

    }

    public CustomEdgeInstanceDTO(CustomEdge customEdge) {
        super(customEdge);
    }

}