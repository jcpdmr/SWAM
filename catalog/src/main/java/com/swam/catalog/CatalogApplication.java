package com.swam.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.qesm.RandomDAGGenerator.PdfType;
import com.qesm.WorkflowType;
import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.type.WorkflowTypeDTO;
import com.swam.commons.mongodb.type.WorkflowTypeDTORepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.swam.commons.mongodb.type")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
@RequiredArgsConstructor
public class CatalogApplication implements CommandLineRunner {

	private final WorkflowTypeDTORepository workflowTypeRepository;

	@SuppressWarnings("unused")
	private final MessageDispatcher requestHandler;

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// TODO: make a service enabled only on local dev profile that generate and save
		// this workflow

		WorkflowType w1 = new WorkflowType();
		w1.generateRandomDAG(5, 5, 3, 3, 50, PdfType.UNIFORM);

		WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO(w1);
		workflowTypeRepository.save(workflowTypeDTO);

	}
}
