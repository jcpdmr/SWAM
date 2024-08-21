package com.swam.catalog;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.RandomDAGGenerator.PdfType;
import com.qesm.WorkflowType;
import com.swam.commons.MessageDispatcher;
import com.swam.commons.mongodb.type.SubWorkflowTypeDTO;
import com.swam.commons.mongodb.type.SubWorkflowTypeRepository;
import com.swam.commons.mongodb.type.WorkflowTypeDTO;
import com.swam.commons.mongodb.type.WorkflowTypeRepository;

@ActiveProfiles("test")
@SpringBootTest()
public class CRUDWorkflowTypeHandlerTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;

    @Autowired
    private SubWorkflowTypeRepository subWorkflowTypeRepository;

    @MockBean
    private MessageDispatcher messageDispatcher;

    @BeforeAll
    private static void dropDb(@Autowired MongoTemplate mongoTemplate) {
        mongoTemplate.getDb().drop();
    }

    @Test
    public void prova() {
        WorkflowType w1 = new WorkflowType();
        w1.generateRandomDAG(5, 5, 3, 3, 50, PdfType.UNIFORM);

        WorkflowTypeDTO workflowTypeDTO = new WorkflowTypeDTO(w1);
        workflowTypeRepository.save(workflowTypeDTO);

        SubWorkflowTypeDTO subWorkflowTypeDTO = new SubWorkflowTypeDTO(w1);
        subWorkflowTypeRepository.save(subWorkflowTypeDTO);
    }

}
