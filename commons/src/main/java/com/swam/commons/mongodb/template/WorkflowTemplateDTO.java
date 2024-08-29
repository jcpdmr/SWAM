package com.swam.commons.mongodb.template;

import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DirectedAcyclicGraph;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.AbstractWorkflow;
import com.qesm.CustomEdge;
import com.qesm.ProductTemplate;
import com.qesm.WorkflowTemplate;
import com.swam.commons.mongodb.AbstractWorkflowDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document("Workflow")
@Getter
@Setter
@ToString(callSuper = true)
public class WorkflowTemplateDTO extends AbstractWorkflowDTO<ProductTemplate> {

    @PersistenceCreator
    public WorkflowTemplateDTO(String id,
            Map<String, ProductTemplateDTO> vertexMap,
            Set<CustomEdgeTemplateDTO> edgeSet) {
        super(id, vertexMap, edgeSet);
    }

    @JsonCreator
    public WorkflowTemplateDTO(@JsonProperty("vertexMap") Map<String, ProductTemplateDTO> vertexMap,
            @JsonProperty("edgeSet") Set<CustomEdgeTemplateDTO> edgeSet) {
        super(vertexMap, edgeSet);
    }

    public WorkflowTemplateDTO(WorkflowTemplate workflowTemplate) {
        super(workflowTemplate);
    }

    @Override
    protected ProductTemplateDTO createProductDTO(ProductTemplate vertex) {
        return new ProductTemplateDTO(vertex);
    }

    @Override
    protected CustomEdgeTemplateDTO createCustomEdgeDTO(CustomEdge edge) {
        return new CustomEdgeTemplateDTO(edge);
    }

    @Override
    protected WorkflowTemplate createWorkflow(DirectedAcyclicGraph<ProductTemplate, CustomEdge> dag) {
        return new WorkflowTemplate(dag);
    }

    @Override
    public AbstractWorkflowDTO<ProductTemplate> buildFromWorkflow(AbstractWorkflow<ProductTemplate> workflow) {
        return new WorkflowTemplateDTO((WorkflowTemplate) workflow);
    }

}
