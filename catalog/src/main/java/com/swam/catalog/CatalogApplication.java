package com.swam.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.qesm.WorkflowType;
import com.qesm.AbstractProduct;
import com.qesm.ProductIstance;
import com.qesm.ProductType;
import com.qesm.RandomDAGGenerator.PdfType;
import com.swam.commons.CustomMessage;
import com.swam.commons.MessageHandler;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.AbstractProductDTO;
import com.swam.commons.ProductRepository;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.MessageHandler.MethodExecutor;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.swam.commons")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
public class CatalogApplication implements CommandLineRunner {

	// @SuppressWarnings("unused")
	// private final WorkflowTypeRepository workflowTypeRepository;

	// @SuppressWarnings("unused")
	// private final ProductIstanceRepository productIstanceRepository;

	@SuppressWarnings("unused")
	private final ProductRepository<ProductType> productRepository;

	@SuppressWarnings("unused")
	private final MessageHandler requestHandler;

	public CatalogApplication(MessageHandler requestHandler, ProductRepository<ProductType> productRepository) {
		this.requestHandler = requestHandler;
		this.productRepository = productRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// MakePersistence testMakePersistence = new MakePersistence();

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

		AbstractProductDTO<ProductType> pt1DTO = new AbstractProductDTO<ProductType>(pt1);
		AbstractProductDTO<ProductType> pt2DTO = new AbstractProductDTO<ProductType>(pt2);

		AbstractProductDTO<ProductIstance> pi1DTO = new AbstractProductDTO<ProductIstance>(pi1);
		AbstractProductDTO<ProductIstance> pi2DTO = new AbstractProductDTO<ProductIstance>(pi2);

		productRepository.save(pt1DTO);
		productRepository.save(pt2DTO);

		productRepository.save(pi1DTO);
		productRepository.save(pi2DTO);

		List<AbstractProductDTO<ProductType>> allProducts = productRepository.findAll();

		for (AbstractProductDTO<ProductType> productDTO : allProducts) {
			System.out.println(productDTO.getIsType());
			try {
				ProductType pt = productDTO.toEdge().get();
				System.out.println("Created ProductType: " + pt);
			} catch (Exception e) {
				try {
					ProductIstance pi = (ProductIstance) productDTO.toEdge().get();
					System.out.println("Created ProductIstance: " + pi);
				} catch (Exception e2) {

				}
			}
		}
	}
}
