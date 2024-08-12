package com.swam.commons;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.WorkflowType;

import lombok.Getter;

@Document
@Getter
public class WorkflowTypeDTO extends AbstractWorkflowDTO<ProductTypeDTO, CustomEdgeTypeDTO> {

    @PersistenceCreator
    public WorkflowTypeDTO(Set<ProductTypeDTO> vertexSet,
            Set<CustomEdgeTypeDTO> edgeSet) {
        super(vertexSet, edgeSet);
    }

    public WorkflowTypeDTO(WorkflowType workflowType) {
        super(workflowType.getDag().vertexSet().stream().map(vertex -> new ProductTypeDTO(vertex))
                .collect(Collectors.toSet()),
                workflowType.getDag().edgeSet().stream().map(edge -> new CustomEdgeTypeDTO(edge))
                        .collect(Collectors.toSet()));
    }

}
