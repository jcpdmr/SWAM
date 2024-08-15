package com.swam.catalog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.oristool.eulero.modeling.stochastictime.UniformTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.qesm.WorkflowType;
import com.qesm.AbstractProduct;
import com.qesm.ProductIstance;
import com.qesm.ProductType;
import com.qesm.RandomDAGGenerator.PdfType;
import com.swam.commons.ProductTypeRepository;
import com.swam.commons.ProductIstanceRepository;
import com.swam.commons.ProductTypeDTO;
import com.swam.commons.WorkflowTypeRepository;
import com.swam.commons.WorkflowIstanceRepository;
import com.swam.commons.CustomMessage;
import com.swam.commons.MessageHandler;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.ProductIstanceDTO;
import com.swam.commons.AbstractProductDTO;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.MessageHandler.MethodExecutor;

import lombok.RequiredArgsConstructor;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.swam.commons")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
@RequiredArgsConstructor
public class CatalogApplication implements CommandLineRunner {

	// @SuppressWarnings("unused")
	// private final WorkflowTypeRepository workflowTypeRepository;

	// @SuppressWarnings("unused")
	// private final ProductIstanceRepository productIstanceRepository;

	private final ProductTypeRepository productTypeRepository;
	private final ProductIstanceRepository productIstanceRepository;
	private final WorkflowTypeRepository workflowTypeRepository;
	private final WorkflowIstanceRepository workflowIstanceRepository;

	@SuppressWarnings("unused")
	private final MessageHandler requestHandler;

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

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

		ProductTypeDTO pt1DTO = new ProductTypeDTO(pt1);
		ProductTypeDTO pt2DTO = new ProductTypeDTO(pt2);

		ProductIstanceDTO pi1DTO = new ProductIstanceDTO(pi1);
		ProductIstanceDTO pi2DTO = new ProductIstanceDTO(pi2);

		productTypeRepository.save(pt1DTO);
		productTypeRepository.save(pt2DTO);

		productIstanceRepository.save(pi1DTO);
		productIstanceRepository.save(pi2DTO);

		List<ProductTypeDTO> allProductTypes = productTypeRepository.findAll();
		List<ProductIstanceDTO> allProductIstances = productIstanceRepository.findAll();

		for (ProductTypeDTO producttypeDTO : allProductTypes) {
			ProductType pt = producttypeDTO.toProduct();
			System.out.println("Created ProductType: " + pt + "     is Type:" + producttypeDTO.getIsType());
		}

		for (ProductIstanceDTO productIstanceDTO : allProductIstances) {
			ProductIstance pt = productIstanceDTO.toProduct();
			System.out.println("Created ProductIstance: " + pt + "     is Type:" + productIstanceDTO.getIsType());
		}
	}
}
