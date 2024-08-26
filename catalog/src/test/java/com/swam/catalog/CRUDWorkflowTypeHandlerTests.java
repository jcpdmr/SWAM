package com.swam.catalog;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.oristool.eulero.modeling.stochastictime.ExponentialTime;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.ProductType;
import com.swam.commons.intercommunication.MessageDispatcher;
import com.swam.commons.mongodb.type.ProductTypeDTO;
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

        // Optional<AbstractProductDTO<?>> productTypeDTO = workflowTypeDTORepository
        // .findVertexByWorkflowIdAndVertexName("test", "v0");

        // System.out.println(productTypeDTO);
        // ProductTypeDTO productTypeDTO = new ProductTypeDTO(new ProductType("v0"));

        // test = workflowTypeDTORepository.updateVertexQuantityProducedAndPdf("test",
        // "v0", 50,
        // new ExponentialTime(BigDecimal.valueOf(10)));

        System.out.println(workflowTypeDTORepository.isProcessed("test", "prova"));

    }

}
