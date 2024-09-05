package com.swam.catalog;

import org.jgrapht.graph.DirectedAcyclicGraph;
import org.oristool.eulero.modeling.stochastictime.UniformTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.qesm.RandomDAGGenerator.PdfType;
import com.qesm.CustomEdge;
import com.qesm.ProductTemplate;
import com.qesm.WorkflowTemplate;
import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.template.WorkflowTemplateDTO;
import com.swam.commons.mongodb.template.WorkflowTemplateDTORepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.swam.commons.mongodb.template")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
@RequiredArgsConstructor
public class CatalogApplication implements CommandLineRunner {

    private final WorkflowTemplateDTORepository workflowTemplateRepository;

    @SuppressWarnings("unused")
    private final MessageDispatcher requestHandler;

    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // TODO: make a service enabled only on local dev profile that generate and save
        // this workflow

        WorkflowTemplate w1 = new WorkflowTemplate();
        w1.generateRandomDAG(5, 5, 3, 3, 50, PdfType.UNIFORM);

        WorkflowTemplateDTO workflowTemplateDTO1 = new WorkflowTemplateDTO(w1);
        workflowTemplateDTO1.setId("random");
        workflowTemplateRepository.save(workflowTemplateDTO1);

        ProductTemplate v0 = new ProductTemplate("v0", 1, new UniformTime(0, 2));
        ProductTemplate v1 = new ProductTemplate("v1", 1, new UniformTime(2, 4));
        ProductTemplate v2 = new ProductTemplate("v2", 1, new UniformTime(4, 6));
        ProductTemplate v3 = new ProductTemplate("v3", 1, new UniformTime(6, 8));
        ProductTemplate v4 = new ProductTemplate("v4", 1, new UniformTime(8, 10));

        ProductTemplate v5 = new ProductTemplate("v5");
        ProductTemplate v6 = new ProductTemplate("v6");
        ProductTemplate v7 = new ProductTemplate("v7");

        DirectedAcyclicGraph<ProductTemplate, CustomEdge> dag = new DirectedAcyclicGraph<>(CustomEdge.class);
        dag.addVertex(v0);
        dag.addVertex(v1);
        dag.addVertex(v2);
        dag.addVertex(v3);
        dag.addVertex(v4);
        dag.addVertex(v5);
        dag.addVertex(v6);
        dag.addVertex(v7);

        dag.addEdge(v1, v0);
        dag.addEdge(v2, v0);
        dag.addEdge(v3, v0);
        dag.addEdge(v4, v2);
        dag.addEdge(v4, v3);
        dag.addEdge(v5, v1);
        dag.addEdge(v6, v4);
        dag.addEdge(v7, v0);

        WorkflowTemplate w2 = new WorkflowTemplate(dag);

        WorkflowTemplateDTO workflowTemplateDTO2 = new WorkflowTemplateDTO(w2);
        workflowTemplateDTO2.setId("test");
        workflowTemplateRepository.save(workflowTemplateDTO2);
    }
}
