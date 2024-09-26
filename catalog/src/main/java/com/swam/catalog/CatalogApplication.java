package com.swam.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.template.WorkflowTemplateTO;
import com.swam.commons.mongodb.template.WorkflowTemplateTORepository;
import com.swam.commons.utility.DefaultWorkflows;

import lombok.RequiredArgsConstructor;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.swam.commons.mongodb.template")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
@RequiredArgsConstructor
public class CatalogApplication implements CommandLineRunner {

    private final WorkflowTemplateTORepository workflowTemplateRepository;

    @SuppressWarnings("unused")
    private final MessageDispatcher requestHandler;

    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        WorkflowTemplateTO workflowTemplateTO1 = DefaultWorkflows.getWorkflowTemplateTO1();
        workflowTemplateTO1.setId("random");
        workflowTemplateRepository.save(workflowTemplateTO1);

        WorkflowTemplateTO workflowTemplateTO2 = DefaultWorkflows.getWorkflowTemplateTO2();
        workflowTemplateTO2.setId("test");
        workflowTemplateRepository.save(workflowTemplateTO2);
    }
}
