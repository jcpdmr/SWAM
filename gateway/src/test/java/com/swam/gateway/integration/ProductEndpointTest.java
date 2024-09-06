package com.swam.gateway.integration;

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
    }
}
