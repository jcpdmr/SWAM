package com.swam.gateway.endToEnd;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.oristool.eulero.modeling.stochastictime.DeterministicTime;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.workflow.ProductTemplate;
import com.qesm.workflow.WorkflowTemplate;
import com.swam.commons.mongodb.template.ProductTemplateEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntity;
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
        checkIfResponseIsEqualsToEntity("/catalog/test/product/v1", new ProductTemplateEntity(DefaultProducts.v1));

        checkIfResponseIsEqualsToEntitySet("/catalog/test/product",
                Set.of(new ProductTemplateEntity(DefaultProducts.v0), new ProductTemplateEntity(DefaultProducts.v1),
                        new ProductTemplateEntity(DefaultProducts.v2), new ProductTemplateEntity(DefaultProducts.v3),
                        new ProductTemplateEntity(DefaultProducts.v4), new ProductTemplateEntity(DefaultProducts.v5),
                        new ProductTemplateEntity(DefaultProducts.v6), new ProductTemplateEntity(DefaultProducts.v7)),
                ProductTemplateEntity.class);

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
                .bodyValue(DefaultWorkflows.getWorkflowTemplateEntity2())
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToEntity("/catalog/random", DefaultWorkflows.getWorkflowTemplateEntity2());

        // Put test
        ProductTemplateEntity v1Updated = new ProductTemplateEntity(
                new ProductTemplate("v1", 150, new DeterministicTime(BigDecimal.valueOf(5.0))));

        client.put().uri("/catalog/random/product/v1")
                .bodyValue(v1Updated)
                .exchange()
                .expectStatus().isOk();

        checkIfResponseIsEqualsToEntity("/catalog/random/product/v1", v1Updated);

        // Delete test
        client.delete().uri("/catalog/random/product/v1")
                .exchange()
                .expectStatus().isOk();

        client.get().uri("/catalog/random/product/v1")
                .exchange()
                .expectStatus().isNotFound();

        try {
            WorkflowTemplate workflowTemplateUpdated = (WorkflowTemplate) DefaultWorkflows.getWorkflowTemplateEntity2()
                    .convertAndValidate();
            workflowTemplateUpdated.removeVertex(workflowTemplateUpdated.findProduct("v1").get());

            checkIfResponseIsEqualsToEntity("/catalog/random", new WorkflowTemplateEntity(workflowTemplateUpdated));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
