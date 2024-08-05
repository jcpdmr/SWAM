package com.swam.catalog;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;
import com.qesm.ProductType;
import com.qesm.WorkflowType;

@Document
public class WorkflowTypeDTO {

    private @Id String id;
    private final Set<ProductTypeDTO> vertexSet;
    private final Set<CustomEdgeDTO> edgeSet;
    private final @Transient HashMap<String, ProductType> nameToProductMap;

    @PersistenceCreator
    public WorkflowTypeDTO(Set<ProductTypeDTO> vertexSet, Set<CustomEdgeDTO> edgeSet) {
        this.vertexSet = vertexSet;
        this.edgeSet = edgeSet;
        this.nameToProductMap = new HashMap<>();
    }

    public WorkflowTypeDTO(WorkflowType workflowType) {
        this.vertexSet = workflowType.getDag().vertexSet().stream().map(vertex -> new ProductTypeDTO(vertex))
                .collect(Collectors.toSet());
        this.edgeSet = workflowType.getDag().edgeSet().stream().map(edge -> new CustomEdgeDTO(edge))
                .collect(Collectors.toSet());
        this.nameToProductMap = new HashMap<>();
    }

    public WorkflowType toWorkflowType() {
        ListenableDAG<ProductType, CustomEdge> dag = new ListenableDAG<>(CustomEdge.class);

        for (ProductTypeDTO productTypeDTO : vertexSet) {
            ProductType productType = productTypeDTO.toProductType();
            nameToProductMap.put(productTypeDTO.getName(), productType);
            dag.addVertex(productType);
        }

        for (CustomEdgeDTO customEdgeDTO : edgeSet) {
            // ProductType sourceVertex = null, targetVertex = null;
            // for (ProductType vertex : dag.vertexSet()) {
            // if (sourceVertex != null && targetVertex != null) {
            // break;
            // } else if (customEdgeDTO.getSource().getName().equals(vertex.getName())) {
            // sourceVertex = vertex;
            // } else if (customEdgeDTO.getTarget().getName().equals(vertex.getName())) {
            // targetVertex = vertex;
            // }
            // }
            CustomEdge customEdge = dag.addEdge(nameToProductMap.get(customEdgeDTO.getSource().getName()),
                    nameToProductMap.get(customEdgeDTO.getTarget().getName()));
            customEdge.setQuantityRequired(customEdgeDTO.getQuantityRequired());
        }

        return new WorkflowType(dag);
    }

    @Override
    public String toString() {
        return vertexSet.toString() + " " + edgeSet.toString();
    }
}
