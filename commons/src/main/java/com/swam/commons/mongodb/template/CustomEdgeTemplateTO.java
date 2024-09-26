package com.swam.commons.mongodb.template;

import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CustomEdgeTemplateTO extends AbstractCustomEdgeTO {

    @PersistenceCreator
    public CustomEdgeTemplateTO(@JsonProperty("sourceName") String sourceName,
            @JsonProperty("targetName") String targetName, @JsonProperty("quantityRequired") Integer quantityRequired) {
        super(sourceName, targetName, quantityRequired);

    }

    public CustomEdgeTemplateTO(CustomEdge customEdge) {
        super(customEdge);
    }

}
