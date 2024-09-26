package com.swam.commons.utility;

import org.jgrapht.graph.DirectedAcyclicGraph;

import com.qesm.CustomEdge;
import com.qesm.ProductTemplate;
import com.qesm.RandomDAGGenerator.PdfType;
import com.qesm.WorkflowTemplate;
import com.swam.commons.mongodb.template.WorkflowTemplateTO;

public class DefaultWorkflows {

    public static WorkflowTemplateTO getWorkflowTemplateTO1() {
        WorkflowTemplate workflowTemplate = new WorkflowTemplate();
        workflowTemplate.generateRandomDAG(5, 5, 3, 3, 50, PdfType.UNIFORM);
        return new WorkflowTemplateTO(workflowTemplate);
    }

    public static WorkflowTemplateTO getWorkflowTemplateTO2() {

        DirectedAcyclicGraph<ProductTemplate, CustomEdge> dag = new DirectedAcyclicGraph<>(CustomEdge.class);
        dag.addVertex(DefaultProducts.v0);
        dag.addVertex(DefaultProducts.v1);
        dag.addVertex(DefaultProducts.v2);
        dag.addVertex(DefaultProducts.v3);
        dag.addVertex(DefaultProducts.v4);
        dag.addVertex(DefaultProducts.v5);
        dag.addVertex(DefaultProducts.v6);
        dag.addVertex(DefaultProducts.v7);

        dag.addEdge(DefaultProducts.v1, DefaultProducts.v0);
        dag.addEdge(DefaultProducts.v2, DefaultProducts.v0);
        dag.addEdge(DefaultProducts.v3, DefaultProducts.v0);
        dag.addEdge(DefaultProducts.v4, DefaultProducts.v2);
        dag.addEdge(DefaultProducts.v4, DefaultProducts.v3);
        dag.addEdge(DefaultProducts.v5, DefaultProducts.v1);
        dag.addEdge(DefaultProducts.v6, DefaultProducts.v4);
        dag.addEdge(DefaultProducts.v7, DefaultProducts.v0);

        WorkflowTemplate workflowTemplate = new WorkflowTemplate(dag);

        return new WorkflowTemplateTO(workflowTemplate);
    }

    public static WorkflowTemplateTO getWorkflowTemplateTO3() {

        DirectedAcyclicGraph<ProductTemplate, CustomEdge> dag = new DirectedAcyclicGraph<>(CustomEdge.class);
        dag.addVertex(DefaultProducts.v0);
        dag.addVertex(DefaultProducts.v5);
        dag.addVertex(DefaultProducts.v6);
        dag.addVertex(DefaultProducts.v7);

        dag.addEdge(DefaultProducts.v5, DefaultProducts.v0);
        dag.addEdge(DefaultProducts.v6, DefaultProducts.v0);
        dag.addEdge(DefaultProducts.v7, DefaultProducts.v0);

        WorkflowTemplate workflowTemplate = new WorkflowTemplate(dag);

        return new WorkflowTemplateTO(workflowTemplate);
    }
}
