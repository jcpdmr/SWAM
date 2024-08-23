package com.swam.catalog;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.RandomDAGGenerator.PdfType;
import com.mongodb.ExplainVerbosity;
import com.qesm.ProductType;
import com.qesm.WorkflowType;
import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.type.WorkflowTypeDTORepository;

@ActiveProfiles("test")
@SpringBootTest()
public class CRUDWorkflowTypeHandlerTests {

    @Autowired
    private WorkflowTypeDTORepository workflowTypeDTORepository;

    @MockBean
    private MessageDispatcher messageDispatcher;

    @MockBean
    private CRUDWorkflowTypeHandler crudWorkflowTypeHandler;

    @BeforeAll
    private static void dropDb(@Autowired MongoTemplate mongoTemplate) {
        // mongoTemplate.getDb().drop();
    }

    @Test
    public void prova() {
        // WorkflowType w1 = new WorkflowType();
        // w1.generateRandomDAG(5, 5, 3, 3, 50, PdfType.UNIFORM);

        // HeadWorkflowTypeDTO workflowTypeDTO = new HeadWorkflowTypeDTO(w1);
        // workflowTypeDTORepository.save(workflowTypeDTO);

        // System.out.println(workflowTypeDTORepository.findSpecificSubWorkflow("66c76bf6c2e10d6b8f800a34",
        // "76a739d2-87d7-493d-90ca-5694aa7c1876"));

        // Optional<HeadWorkflowTypeDTO> headWorkflowTypeDTO = workflowTypeDTORepository
        // .findById("66c76bf6c2e10d6b8f800a34");
        // System.out.println(headWorkflowTypeDTO.get());

        // List<AbstractBaseWorkflowDTO<ProductType>> subWorkflows =
        // headWorkflowTypeDTO.get().getSubWorkflowDTOList();
        // System.out.println("Sub-workflows: " + subWorkflows);

        // ExplainVerbosity verbosity = ExplainVerbosity.EXECUTION_STATS;
        // Document explainResult = mongoTemplate.getCollection("Workflow")
        // .find(query.getQueryObject())
        // .projection(query.getFieldsObject())
        // .explain(verbosity);
        // System.out.println(explainResult.toJson());

        // for (AbstractWorkflowDTO<ProductType, WorkflowType> subWDTO :
        // result.getSubWorkflowDTOList()) {
        // System.out.println(subWDTO.getId());
        // System.out.println(subWDTO.getVertexSet().size());
        // System.out.println(subWDTO.getEdgeSet().size());
        // }

    }

}
