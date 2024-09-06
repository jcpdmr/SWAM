package com.swam.gateway.integration;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.swam.commons.mongodb.template.ProductTemplateDTO;
import com.swam.commons.utility.DefaultProducts;

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

}
