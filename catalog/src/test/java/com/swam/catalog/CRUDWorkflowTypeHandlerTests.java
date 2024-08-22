package com.swam.catalog;

import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.RandomDAGGenerator.PdfType;
import com.mongodb.ExplainVerbosity;
import com.qesm.ProductType;
import com.qesm.WorkflowType;
import com.swam.commons.MessageDispatcher;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.type.WorkflowTypeDTO;
import com.swam.commons.mongodb.type.WorkflowTypeRepository;

@ActiveProfiles("test")
@SpringBootTest()
public class CRUDWorkflowTypeHandlerTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;

    @MockBean
    private MessageDispatcher messageDispatcher;

    @BeforeAll
    private static void dropDb(@Autowired MongoTemplate mongoTemplate) {
        // mongoTemplate.getDb().drop();
    }

    @Test
    public void prova() {
        WorkflowType w1 = new WorkflowType();
        w1.generateRandomDAG(5, 5, 3, 3, 50, PdfType.UNIFORM);

        WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO(w1);
        workflowTypeRepository.save(workflowTypeDTO);

        // Query query = new Query(
        // Criteria.where("_id").is("66c72346c543a265761e16cc")
        // .and("subWorkflowDTOList._id").is("433b4070-0b8d-42a0-a1c9-470f1260d459"));
        // query.fields().include("subWorkflowDTOList.$");

        // WorkflowTypeDTO result = mongoTemplate.findOne(query, WorkflowTypeDTO.class);

        // System.out.println(result.getSubWorkflowDTOList());

        System.out.println(workflowTypeRepository.findByIdAndSubWorkflowDTOListId("66c72346c543a265761e16cc",
                "433b4070-0b8d-42a0-a1c9-470f1260d459"));

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
