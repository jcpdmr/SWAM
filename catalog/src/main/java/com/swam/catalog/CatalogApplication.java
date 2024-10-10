package com.swam.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.template.WorkflowTemplateEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntityRepository;
import com.swam.commons.utility.DefaultWorkflows;

import lombok.RequiredArgsConstructor;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.swam.commons.mongodb.template")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
@RequiredArgsConstructor
public class CatalogApplication implements CommandLineRunner {

    private final WorkflowTemplateEntityRepository workflowTemplateRepository;

    @SuppressWarnings("unused")
    private final MessageDispatcher requestHandler;

    public static void main(String[] args) {
        SpringApplication.run(CatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        WorkflowTemplateEntity workflowTemplateEntity1 = DefaultWorkflows.getWorkflowTemplateEntity1();
        workflowTemplateEntity1.setId("random");
        workflowTemplateRepository.save(workflowTemplateEntity1);

        WorkflowTemplateEntity workflowTemplateEntity2 = DefaultWorkflows.getWorkflowTemplateEntity2();
        workflowTemplateEntity2.setId("test");
        workflowTemplateRepository.save(workflowTemplateEntity2);

        WorkflowTemplateEntity workflowTemplateEntity4 = DefaultWorkflows.getWorkflowTemplateEntity4();
        workflowTemplateEntity4.setId("demo");
        workflowTemplateRepository.save(workflowTemplateEntity4);
    }
}
