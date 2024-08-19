package com.swam.operation;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.qesm.ListenableDAG;
import com.qesm.ProductIstance;
import com.qesm.WorkflowIstance;
import com.swam.commons.AbstractWorkflowDTO;

import lombok.Getter;

@Document
@Getter
public class WorkflowIstanceDTO extends AbstractWorkflowDTO<ProductIstanceDTO, CustomEdgeIstanceDTO, ProductIstance> {

    @PersistenceCreator
    public WorkflowIstanceDTO(Set<ProductIstanceDTO> vertexSet,
            Set<CustomEdgeIstanceDTO> edgeSet) {
        super(vertexSet, edgeSet);
    }

    public WorkflowIstanceDTO(WorkflowIstance workflowIstance) {
        super(workflowIstance.getDag().vertexSet().stream().map(vertex -> new ProductIstanceDTO(vertex))
                .collect(Collectors.toSet()),
                workflowIstance.getDag().edgeSet().stream().map(edge -> new CustomEdgeIstanceDTO(edge))
                        .collect(Collectors.toSet()));
    }

    @Override
    public WorkflowIstance toWorkflow() {

        ListenableDAG<ProductIstance, CustomEdge> dag = new ListenableDAG<>(CustomEdge.class);

        for (ProductIstanceDTO productIstanceDTO : vertexSet) {
            ProductIstance productIstance = productIstanceDTO.toProduct();
            nameToProductMap.put(productIstanceDTO.getName(), productIstance);
            dag.addVertex(productIstance);
        }

        for (CustomEdgeIstanceDTO customEdgeDTO : edgeSet) {
            CustomEdge customEdge = dag.addEdge(nameToProductMap.get(customEdgeDTO.getSource().getName()),
                    nameToProductMap.get(customEdgeDTO.getTarget().getName()));
            customEdge.setQuantityRequired(customEdgeDTO.getQuantityRequired());
        }

        return new WorkflowIstance(dag);

    }

}
