package com.swam.gateway.integration;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.oristool.eulero.modeling.stochastictime.DeterministicTime;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.ProductTemplate;
import com.qesm.WorkflowTemplate;
import com.swam.commons.mongodb.template.ProductTemplateDTO;
import com.swam.commons.mongodb.template.WorkflowTemplateDTO;
import com.swam.commons.utility.DefaultProducts;
import com.swam.commons.utility.DefaultWorkflows;

@ActiveProfiles("test")
public class ProductEndpointTest extends BaseEndpointTest {

    public ProductEndpointTest() {
        super("http://localhost:8080/api/workflow");
    }

    // Test CRUD operations
    @Test
    public void productGetTest() {
        checkIfResponseIsEqualsToDTO("/catalog/test/product/v1", new ProductTemplateDTO(DefaultProducts.v1));

        checkIfResponseIsEqualsToDTOSet("/catalog/test/product",
                Set.of(new ProductTemplateDTO(DefaultProducts.v0), new ProductTemplateDTO(DefaultProducts.v1),
                        new ProductTemplateDTO(DefaultProducts.v2), new ProductTemplateDTO(DefaultProducts.v3),
                        new ProductTemplateDTO(DefaultProducts.v4), new ProductTemplateDTO(DefaultProducts.v5),
                        new ProductTemplateDTO(DefaultProducts.v6), new ProductTemplateDTO(DefaultProducts.v7)),
                ProductTemplateDTO.class);

        client.get().uri("/catalog/test1/product")
                .exchange()
                .expectStatus().isNotFound();

        client.get().uri("/catalog/test/product/v10")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void productPutAndDeleteTest() {
        // Update random workflow
        client.put().uri("/catalog/random")
                .bodyValue(DefaultWorkflows.getWorkflowTemplateDTO2())
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToDTO("/catalog/random", DefaultWorkflows.getWorkflowTemplateDTO2());

        // Put test
        ProductTemplateDTO v1Updated = new ProductTemplateDTO(
                new ProductTemplate("v1", 150, new DeterministicTime(BigDecimal.valueOf(5.0))));

        client.put().uri("/catalog/random/product/v1")
                .bodyValue(v1Updated)
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToDTO("/catalog/random/product/v1", v1Updated);

        // Delete test
        client.delete().uri("/catalog/random/product/v1")
                .exchange()
                .expectStatus().isOk();

        client.get().uri("/catalog/random/product/v1")
                .exchange()
                .expectStatus().isNotFound();

        try {
            WorkflowTemplate workflowTemplateUpdated = (WorkflowTemplate) DefaultWorkflows.getWorkflowTemplateDTO2()
                    .convertAndValidate();
            workflowTemplateUpdated.removeVertex(workflowTemplateUpdated.findProduct("v1").get());

            checkIfResponseIsEqualsToDTO("/catalog/random", new WorkflowTemplateDTO(workflowTemplateUpdated));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
