package com.swam.commons.mongodb;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;
import com.qesm.ProductType;
import com.qesm.WorkflowType;

import lombok.Getter;

@Document
@Getter
public class WorkflowTypeDTO extends AbstractWorkflowDTO<ProductTypeDTO, CustomEdgeTypeDTO, ProductType> {

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

    @Override
    public WorkflowType toWorkflow() {

        ListenableDAG<ProductType, CustomEdge> dag = new ListenableDAG<>(CustomEdge.class);

        for (ProductTypeDTO productTypeDTO : vertexSet) {
            ProductType productType = productTypeDTO.toProduct();
            idToProductMap.put(productTypeDTO.getId(), productType);
            dag.addVertex(productType);
        }

        for (CustomEdgeTypeDTO customEdgeDTO : edgeSet) {
            CustomEdge customEdge = dag.addEdge(idToProductMap.get(customEdgeDTO.getSourceId()),
                    idToProductMap.get(customEdgeDTO.getTargetId()));
            customEdge.setQuantityRequired(customEdgeDTO.getQuantityRequired());
        }

        return new WorkflowType(dag);

    }

}
