package com.swam.commons.mongodb.instance;

import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CustomEdgeInstanceTO extends AbstractCustomEdgeTO {

    @PersistenceCreator
    @JsonCreator
    public CustomEdgeInstanceTO(@JsonProperty("sourceName") String sourceName,
            @JsonProperty("targetName") String targetName, @JsonProperty("quantityRequired") Integer quantityRequired) {
        super(sourceName, targetName, quantityRequired);

    }

    public CustomEdgeInstanceTO(CustomEdge customEdge) {
        super(customEdge);
    }

}