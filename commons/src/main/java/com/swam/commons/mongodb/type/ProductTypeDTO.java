package com.swam.commons.mongodb.type;

import java.util.UUID;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.AbstractProduct.ItemGroup;
import com.swam.commons.mongodb.AbstractProductDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.ProductType;

import lombok.Getter;

@Document
@Getter
public class ProductTypeDTO extends AbstractProductDTO<ProductType> {

    @PersistenceCreator
    @JsonCreator
    public ProductTypeDTO(@JsonProperty("name") String name, @JsonProperty("id") String id,
            @JsonProperty("quantityProduced") Integer quantityProduced, @JsonProperty("pdf") StochasticTime pdf,
            @JsonProperty("itemGroup") ItemGroup itemGroup) {
        super(name, id, quantityProduced, pdf, itemGroup);
    }

    public ProductTypeDTO(ProductType product) {
        super(product);
    }

    @Override
    public ProductType createProduct(UUID uuid) {
        return new ProductType(uuid);
    }

}
