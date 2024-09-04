package com.swam.catalog;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.oristool.eulero.modeling.stochastictime.ExponentialTime;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.ProductTemplate;
import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.template.ProductTemplateDTO;
import com.swam.commons.mongodb.template.WorkflowTemplateDTORepository;

@ActiveProfiles("test")
@SpringBootTest()
public class CRUDWorkflowTemplateHandlerTests {

    @Autowired
    private WorkflowTemplateDTORepository workflowTemplateDTORepository;

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

        // Optional<AbstractProductDTO<?>> productTemplateDTO =
        // workflowTemplateDTORepository
        // .findVertexByWorkflowIdAndVertexName("test", "v0");

        // System.out.println(productTemplateDTO);
        // ProductTemplateDTO productTemplateDTO = new ProductTemplateDTO(new
        // ProductTemplate("v0"));

        // test =
        // workflowTemplateDTORepository.updateVertexQuantityProducedAndPdf("test",
        // "v0", 50,
        // new ExponentialTime(BigDecimal.valueOf(10)));

        System.out.println(workflowTemplateDTORepository.existVertexAndIsProcessed("test", "prova"));
        System.out.println(workflowTemplateDTORepository.findWorkflowIfVertexExists("test", "v0"));
    }

}
