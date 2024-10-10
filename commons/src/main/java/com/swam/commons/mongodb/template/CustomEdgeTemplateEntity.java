package com.swam.commons.mongodb.template;

import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.workflow.CustomEdge;
import com.swam.commons.mongodb.AbstractCustomEdgeEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CustomEdgeTemplateEntity extends AbstractCustomEdgeEntity {

    @PersistenceCreator
    public CustomEdgeTemplateEntity(@JsonProperty("sourceName") String sourceName,
            @JsonProperty("targetName") String targetName, @JsonProperty("quantityRequired") Integer quantityRequired) {
        super(sourceName, targetName, quantityRequired);

    }

    public CustomEdgeTemplateEntity(CustomEdge customEdge) {
        super(customEdge);
    }

}
