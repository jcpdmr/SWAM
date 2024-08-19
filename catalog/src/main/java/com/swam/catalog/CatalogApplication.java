package com.swam.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.qesm.RandomDAGGenerator.PdfType;
import com.qesm.WorkflowType;
import com.swam.commons.MessageDispatcher;

import lombok.RequiredArgsConstructor;

@SpringBootApplication()
@EnableMongoRepositories(basePackages = "com.swam.catalog")
@ComponentScan(basePackages = { "com.swam.commons", "com.swam.catalog" })
@RequiredArgsConstructor
public class CatalogApplication implements CommandLineRunner {

	// private final ProductTypeRepository productTypeRepository;
	private final WorkflowTypeRepository workflowTypeRepository;

	@SuppressWarnings("unused")
	private final MessageDispatcher requestHandler;

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// System.out.println("test");

		WorkflowType w1 = new WorkflowType();
		w1.generateRandomDAG(5, 5, 3, 3, 50, PdfType.UNIFORM);

		WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO(w1);
		workflowTypeRepository.save(workflowTypeDTO);

		// List<WorkflowTypeDTO> resultList = workflowTypeRepository.findAll();
		// WorkflowType w1FromQuery = resultList.get(0).toWorkflow();
		// System.out.println("I workflow sono uguali? : " + w1.equals(w1FromQuery));

		// ProductType pt1 = new ProductType("pt1", 1, new UniformTime(1, 2));
		// ProductType pt2 = new ProductType("pt2", 1, new UniformTime(1, 2));
		// System.out.println(pt1);
		// System.out.println(pt2);

		// ProductIstance pi1 = new ProductIstance(pt1);
		// ProductIstance pi2 = new ProductIstance(pt2);
		// System.out.println(pi1);
		// System.out.println(pi2);

		// ProductTypeDTO pt1DTO = new ProductTypeDTO(pt1);
		// ProductTypeDTO pt2DTO = new ProductTypeDTO(pt2);

		// ProductIstanceDTO pi1DTO = new ProductIstanceDTO(pi1);
		// ProductIstanceDTO pi2DTO = new ProductIstanceDTO(pi2);

		// productTypeRepository.save(pt1DTO);
		// productTypeRepository.save(pt2DTO);

		// productIstanceRepository.save(pi1DTO);
		// productIstanceRepository.save(pi2DTO);

		// List<ProductTypeDTO> allProductTypesDTO = productTypeRepository.findAll();
		// List<ProductIstanceDTO> allProductIstancesDTO =
		// productIstanceRepository.findAll();
		// List<ProductType> allProductTypes = new ArrayList<ProductType>();
		// List<ProductIstance> allProductIstances = new ArrayList<ProductIstance>();

		// for (ProductTypeDTO producttypeDTO : allProductTypesDTO) {
		// ProductType pt = producttypeDTO.toProduct();
		// System.out.println("Created ProductType: " + pt + " is Type:" +
		// producttypeDTO.getIsType());
		// allProductTypes.add(pt);
		// }

		// for (ProductIstanceDTO productIstanceDTO : allProductIstancesDTO) {
		// ProductIstance pi = productIstanceDTO.toProduct();
		// System.out.println("Created ProductIstance: " + pi + " is Type:" +
		// productIstanceDTO.getIsType());
		// allProductIstances.add(pi);
		// }

		// for (ProductType pt : allProductTypes) {
		// for (ProductIstance pi : allProductIstances) {
		// System.out.println("ProdIst: " + pi + " - ProdType: " + pt + " ------->
		// Equals:" + pi.equals(pt));
		// }

		// }
	}
}
