package com.swam.catalog;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.qesm.WorkflowType;
import com.qesm.RandomDAGGenerator.PdfType;

@SpringBootApplication()
@ComponentScan(basePackages = { "com.swam.multimodule" })
public class CatalogApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Autowired
	private WorkflowTypeRepository workflowTypeRepository;

	@Override
	public void run(String... args) throws Exception {

		WorkflowType w1 = new WorkflowType();
		w1.generateRandomDAG(3, 3, 2, 2, 50, PdfType.UNIFORM);
		System.out.println(w1);
		WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO(w1);

		workflowTypeRepository.save(workflowTypeDTO);
		List<WorkflowTypeDTO> resultList = workflowTypeRepository.findAll();
		WorkflowType w1FromQuery = resultList.get(0).toWorkflowType();
		System.out.println(w1FromQuery);
		System.out.println("I workflow sono uguali? : " + w1.equals(w1FromQuery));

	}

	@RabbitListener(queues = "catalog_in")
	public void requestHandler(String msg) {
		System.out.println("Messaggio ricevuto dall'handler: " + msg);
	}
}
