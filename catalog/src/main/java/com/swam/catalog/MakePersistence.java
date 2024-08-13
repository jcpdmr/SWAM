package com.swam.catalog;

import com.swam.commons.MessageHandler.MethodExecutor;

import org.oristool.eulero.modeling.stochastictime.UniformTime;
import org.springframework.stereotype.Service;

import com.qesm.ProductType;
import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.ProductDTO;

@Service
public class MakePersistence implements MethodExecutor {

    @Override
    public void execute(CustomMessage context) {
        // TODO: implement method
        // System.out.println("Execute MakePersistence");
        // if (context.getRequestBody().isPresent()) {
        // System.out.println("RequestBody: " + context.getRequestBody().get());
        // }
        // if (context.getQueryParams().isPresent()) {
        // System.out.println("RequestParam: " + context.getQueryParams().get());
        // }

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
        // ProductType pt2 = new ProductType("pt2", 1, new UniformTime(1, 2));
        System.out.println(pt1);
        // System.out.println(pt2);

        // ProductIstance pi1 = new ProductIstance(pt1);
        // ProductIstance pi2 = new ProductIstance(pt2);
        // System.out.println(pi1);
        // System.out.println(pi2);

        ProductDTO<ProductType> pt1DTO = new ProductDTO<ProductType>(pt1);
        // ProductDTO<ProductType> pt2DTO = new ProductDTO<ProductType>(pt2);

        // ProductDTO<ProductIstance> pi1DTO = new ProductDTO<ProductIstance>(pi1);
        // ProductDTO<ProductIstance> pi2DTO = new ProductDTO<ProductIstance>(pi2);

        // productTypeRepository.save(pt1DTO);
        // productTypeRepository.save(pt2DTO);

        // productIstanceRepository.save(pi1DTO);
        // productIstanceRepository.save(pi2DTO);

        // ProductType reconstructedProduct = pt1DTO.toProduct(ProductType.class);
        // System.out.println(reconstructedProduct);

    }

    @Override
    public TargetMethods getBinding() {
        return TargetMethods.MAKE_PERSISTENCE;
    }

}
