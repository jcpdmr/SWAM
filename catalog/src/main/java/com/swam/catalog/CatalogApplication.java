package com.swam.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.swam.commons.MessageHandler;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.swam.commons")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
public class CatalogApplication implements CommandLineRunner {

	@SuppressWarnings("unused")
	private final WorkflowTypeRepository workflowTypeRepository;

	@SuppressWarnings("unused")
	private final MessageHandler requestHandler;

	public CatalogApplication(WorkflowTypeRepository workflowTypeRepository,
			MessageHandler requestHandler) {
		this.workflowTypeRepository = workflowTypeRepository;
		this.requestHandler = requestHandler;
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// MakePersistence testMakePersistence = new MakePersistence();

		// testMakePersistence.execute(null);

	}
}
