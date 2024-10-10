package com.swam.catalog;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.oristool.eulero.modeling.stochastictime.ExponentialTime;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.workflow.ProductTemplate;
import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.template.ProductTemplateEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntityRepository;

@ActiveProfiles("test")
@SpringBootTest()
public class CRUDWorkflowTemplateHandlerTests {

    @Autowired
    private WorkflowTemplateEntityRepository workflowTemplateEntityRepository;

    @MockBean
    private MessageDispatcher messageDispatcher;

    @MockBean
    private CRUDWorkflowTemplateHandler crudWorkflowTemplateHandler;

    @BeforeAll
    private static void dropDb(@Autowired MongoTemplate mongoTemplate) {
        // mongoTemplate.getDb().drop();
    }

    @Test
    public void prova() {

        // Optional<AbstractProductEntity<?>> productTemplateEntity =
        // workflowTemplateEntityRepository
        // .findVertexByWorkflowIdAndVertexName("test", "v0");

        // System.out.println(productTemplateEntity);
        // ProductTemplateEntity productTemplateEntity = new ProductTemplateEntity(new
        // ProductTemplate("v0"));

        // test =
        // workflowTemplateEntityRepository.updateVertexQuantityProducedAndPdf("test",
        // "v0", 50,
        // new ExponentialTime(BigDecimal.valueOf(10)));

        System.out.println(workflowTemplateEntityRepository.existVertexAndIsProcessed("test", "prova"));
        System.out.println(workflowTemplateEntityRepository.findWorkflowIfVertexExists("test", "v0"));
    }

}
