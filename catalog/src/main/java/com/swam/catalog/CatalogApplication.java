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

import com.qesm.WorkflowType;
import com.qesm.ProductIstance;
import com.qesm.ProductType;
import com.qesm.RandomDAGGenerator.PdfType;
import com.swam.commons.CustomMessage;
import com.swam.commons.MessageHandler;
import com.swam.commons.OrchestratorInfo;
import com.swam.commons.ProductDTO;
import com.swam.commons.RabbitMQSender;
import com.swam.commons.MessageHandler.MethodExecutor;

@SpringBootApplication()
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
public class CatalogApplication implements CommandLineRunner {

	@SuppressWarnings("unused")
	private final WorkflowTypeRepository workflowTypeRepository;

	@SuppressWarnings("unused")
	private final ProductIstanceRepository productIstanceRepository;

	@SuppressWarnings("unused")
	private final ProductTypeRepository productTypeRepository;

	@SuppressWarnings("unused")
	private final MessageHandler requestHandler;

	public CatalogApplication(WorkflowTypeRepository workflowTypeRepository,
			ProductIstanceRepository productIstanceRepository, ProductTypeRepository productTypeRepository,
			MessageHandler requestHandler) {
		this.workflowTypeRepository = workflowTypeRepository;
		this.productIstanceRepository = productIstanceRepository;
		this.productTypeRepository = productTypeRepository;
		this.requestHandler = requestHandler;
	}

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

		// ProductType pt1 = new ProductType("pt1", 1, new UniformTime(1, 2));
		// ProductType pt2 = new ProductType("pt2", 1, new UniformTime(1, 2));
		// System.out.println(pt1);
		// System.out.println(pt2);

		// ProductIstance pi1 = new ProductIstance(pt1);
		// ProductIstance pi2 = new ProductIstance(pt2);
		// System.out.println(pi1);
		// System.out.println(pi2);

		// ProductDTO<ProductType> pt1DTO = new ProductDTO<ProductType>(pt1);
		// ProductDTO<ProductType> pt2DTO = new ProductDTO<ProductType>(pt2);

		// ProductDTO<ProductIstance> pi1DTO = new ProductDTO<ProductIstance>(pi1);
		// ProductDTO<ProductIstance> pi2DTO = new ProductDTO<ProductIstance>(pi2);

		// productTypeRepository.save(pt1DTO);
		// productTypeRepository.save(pt2DTO);

		// productIstanceRepository.save(pi1DTO);
		// productIstanceRepository.save(pi2DTO);

	}
}
