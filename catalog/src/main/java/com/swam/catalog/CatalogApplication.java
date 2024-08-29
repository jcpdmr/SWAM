package com.swam.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.qesm.RandomDAGGenerator.PdfType;
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

        WorkflowTemplateDTO workflowTemplateDTO = new WorkflowTemplateDTO(w1);
        workflowTemplateDTO.setId("test");
        workflowTemplateRepository.save(workflowTemplateDTO);

    }
}
