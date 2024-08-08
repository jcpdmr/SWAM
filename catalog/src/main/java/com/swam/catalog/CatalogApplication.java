package com.swam.catalog;

import java.util.List;

import org.oristool.eulero.modeling.stochastictime.UniformTime;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.swam.multimodule.commons.OrchestratorInfo;
import com.swam.multimodule.commons.ProductDTO;
import com.swam.multimodule.commons.RabbitMQSenderMicroservices;
import com.qesm.ProductIstance;
import com.qesm.ProductType;
import com.qesm.WorkflowType;
import com.qesm.RandomDAGGenerator.PdfType;;

@SpringBootApplication()
@ComponentScan(basePackages = { "com.swam.multimodule" })
public class CatalogApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Autowired
	private WorkflowTypeRepository workflowTypeRepository;

	@Autowired
	private ProductIstanceRepository productIstanceRepository;

	@Autowired
	private ProductTypeRepository productTypeRepository;

	@Autowired
	private RabbitMQSenderMicroservices rabbitMQSenderMicroservices;

	@Override
	public void run(String... args) throws Exception {

		// WorkflowType w1 = new WorkflowType();
		// w1.generateRandomDAG(3, 3, 2, 2, 50, PdfType.UNIFORM);
		// System.out.println(w1);
		// WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO(w1);

		// workflowTypeRepository.save(workflowTypeDTO);
		// List<WorkflowTypeDTO> resultList = workflowTypeRepository.findAll();
		// WorkflowType w1FromQuery = resultList.get(0).toWorkflowType();
		// System.out.println(w1FromQuery);
		// System.out.println("I workflow sono uguali? : " + w1.equals(w1FromQuery));

		ProductType pt1 = new ProductType("pt1", 1, new UniformTime(1, 2));
		ProductType pt2 = new ProductType("pt2", 1, new UniformTime(1, 2));
		System.out.println(pt1);
		System.out.println(pt2);

		ProductIstance pi1 = new ProductIstance(pt1);
		ProductIstance pi2 = new ProductIstance(pt2);
		System.out.println(pi1);
		System.out.println(pi2);

		ProductDTO<ProductType> pt1DTO = new ProductDTO<ProductType>(pt1);
		ProductDTO<ProductType> pt2DTO = new ProductDTO<ProductType>(pt2);

		ProductDTO<ProductIstance> pi1DTO = new ProductDTO<ProductIstance>(pi1);
		ProductDTO<ProductIstance> pi2DTO = new ProductDTO<ProductIstance>(pi2);

		productTypeRepository.save(pt1DTO);
		productTypeRepository.save(pt2DTO);

		productIstanceRepository.save(pi1DTO);
		productIstanceRepository.save(pi2DTO);
	}

	// @RabbitListener(queues = "catalog_in", messageConverter = "simpleConverter")
	// public void requestHandler(Message msg) {
	// 	System.out.println("Messaggio ricevuto dall'handler: " + msg);
	// 	OrchestratorInfo info = new OrchestratorInfo(msg.getMessageProperties());
	// 	System.out.println(info);
	// 	rabbitMQSenderMicroservices.sendToNextHop(msg);
	// }
}
