package com.swam.catalog;

import java.util.List;

import org.springframework.stereotype.Service;
import com.swam.commons.intercommunication.MessageDispatcher.MessageHandler;
import com.swam.commons.intercommunication.CustomMessage;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.type.WorkflowTypeDTORepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MakePersistence implements MessageHandler {

    private final WorkflowTypeDTORepository workflowTypeDTORepository;

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) {
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

        // ProductType pt1 = new ProductType("pt1", 1, new UniformTime(1, 2));
        // ProductType pt2 = new ProductType("pt2", 1, new UniformTime(1, 2));
        // System.out.println(pt1);
        // System.out.println(pt2);

        // ProductInstance pi1 = new ProductInstance(pt1);
        // ProductInstance pi2 = new ProductInstance(pt2);
        // System.out.println(pi1);
        // System.out.println(pi2);

        // ProductDTO<ProductType> pt1DTO = new ProductDTO<ProductType>(pt1);
        // ProductDTO<ProductType> pt2DTO = new ProductDTO<ProductType>(pt2);

        // ProductDTO<ProductInstance> pi1DTO = new ProductDTO<ProductInstance>(pi1);
        // ProductDTO<ProductInstance> pi2DTO = new ProductDTO<ProductInstance>(pi2);

        // productTypeRepository.save(pt1DTO);
        // productTypeRepository.save(pt2DTO);

        // productInstanceRepository.save(pi1DTO);
        // productInstanceRepository.save(pi2DTO);

        // ProductType reconstructedProduct = pt1DTO.toProduct(ProductType.class);
        // System.out.println(reconstructedProduct);

        // WorkflowType workflowType = new WorkflowType();
        // workflowType.generateRandomDAG(5, 5, 3, 3, 50, PdfType.UNIFORM);

        // WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO(workflowType);
        // workflowTypeRepository.save(workflowTypeDTO);

    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.MAKE_PERSISTENCE);
    }

}
