package com.swam.commons.mongodb.template;

import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CustomEdgeTemplateDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeTemplateDTO(@JsonProperty("sourceName") String sourceName,
            @JsonProperty("targetName") String targetName, @JsonProperty("quantityRequired") Integer quantityRequired) {
        super(sourceName, targetName, quantityRequired);

    }

    public CustomEdgeTemplateDTO(CustomEdge customEdge) {
        super(customEdge);
    }

}
