package com.swam.commons.mongodb.type;

import java.util.Set;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.WorkflowType;

@Document("SubWorkflow")
public class SubWorkflowTypeDTO extends WorkflowTypeDTO {

    @PersistenceCreator
    public SubWorkflowTypeDTO(String id, Set<ProductTypeDTO> vertexSet,
            Set<CustomEdgeTypeDTO> edgeSet) {
        super(id, vertexSet, edgeSet);
    }

    public SubWorkflowTypeDTO(WorkflowType workflowType) {
        super(workflowType);
    }

}
