package com.swam.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.template.WorkflowTemplateDTO;
import com.swam.commons.mongodb.template.WorkflowTemplateDTORepository;
import com.swam.commons.utility.DefaultWorkflows;

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

        WorkflowTemplateDTO workflowTemplateDTO1 = DefaultWorkflows.getWorkflowTemplateDTO1();
        workflowTemplateDTO1.setId("random");
        workflowTemplateRepository.save(workflowTemplateDTO1);

        WorkflowTemplateDTO workflowTemplateDTO2 = DefaultWorkflows.getWorkflowTemplateDTO2();
        workflowTemplateDTO2.setId("test");
        workflowTemplateRepository.save(workflowTemplateDTO2);
    }
}
