package com.swam.commons.mongodb.template;

import java.util.Map;
import java.util.Set;

import org.jgrapht.graph.DirectedAcyclicGraph;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.workflow.AbstractWorkflow;
import com.qesm.workflow.CustomEdge;
import com.qesm.workflow.ProductTemplate;
import com.qesm.workflow.WorkflowTemplate;
import com.swam.commons.mongodb.AbstractWorkflowEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document("Workflow")
@Getter
@Setter
@ToString(callSuper = true)
public class WorkflowTemplateEntity extends AbstractWorkflowEntity<ProductTemplate> {

    @PersistenceCreator
    public WorkflowTemplateEntity(String id,
            Map<String, ProductTemplateEntity> vertexMap,
            Set<CustomEdgeTemplateEntity> edgeSet) {
        super(id, vertexMap, edgeSet);
    }

    @JsonCreator
    public WorkflowTemplateEntity(@JsonProperty("vertexMap") Map<String, ProductTemplateEntity> vertexMap,
            @JsonProperty("edgeSet") Set<CustomEdgeTemplateEntity> edgeSet) {
        super(vertexMap, edgeSet);
    }

    public WorkflowTemplateEntity(WorkflowTemplate workflowTemplate) {
        super(workflowTemplate);
    }

    @Override
    protected ProductTemplateEntity createProductEntity(ProductTemplate vertex) {
        return new ProductTemplateEntity(vertex);
    }

    @Override
    protected CustomEdgeTemplateEntity createCustomEdgeEntity(CustomEdge edge) {
        return new CustomEdgeTemplateEntity(edge);
    }

    @Override
    protected WorkflowTemplate createWorkflow(DirectedAcyclicGraph<ProductTemplate, CustomEdge> dag) {
        return new WorkflowTemplate(dag);
    }

    @Override
    public AbstractWorkflowEntity<ProductTemplate> buildFromWorkflow(AbstractWorkflow<ProductTemplate> workflow) {
        return new WorkflowTemplateEntity((WorkflowTemplate) workflow);
    }

}
