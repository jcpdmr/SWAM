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
import com.swam.commons.mongodb.AbstractWorkflowTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document("Workflow")
@Getter
@Setter
@ToString(callSuper = true)
public class WorkflowTemplateTO extends AbstractWorkflowTO<ProductTemplate> {

    @PersistenceCreator
    public WorkflowTemplateTO(String id,
            Map<String, ProductTemplateTO> vertexMap,
            Set<CustomEdgeTemplateTO> edgeSet) {
        super(id, vertexMap, edgeSet);
    }

    @JsonCreator
    public WorkflowTemplateTO(@JsonProperty("vertexMap") Map<String, ProductTemplateTO> vertexMap,
            @JsonProperty("edgeSet") Set<CustomEdgeTemplateTO> edgeSet) {
        super(vertexMap, edgeSet);
    }

    public WorkflowTemplateTO(WorkflowTemplate workflowTemplate) {
        super(workflowTemplate);
    }

    @Override
    protected ProductTemplateTO createProductTO(ProductTemplate vertex) {
        return new ProductTemplateTO(vertex);
    }

    @Override
    protected CustomEdgeTemplateTO createCustomEdgeTO(CustomEdge edge) {
        return new CustomEdgeTemplateTO(edge);
    }

    @Override
    protected WorkflowTemplate createWorkflow(DirectedAcyclicGraph<ProductTemplate, CustomEdge> dag) {
        return new WorkflowTemplate(dag);
    }

    @Override
    public AbstractWorkflowTO<ProductTemplate> buildFromWorkflow(AbstractWorkflow<ProductTemplate> workflow) {
        return new WorkflowTemplateTO((WorkflowTemplate) workflow);
    }

}
