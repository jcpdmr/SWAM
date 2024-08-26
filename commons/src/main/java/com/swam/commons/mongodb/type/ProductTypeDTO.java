package com.swam.commons.mongodb.type;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;

import com.qesm.AbstractProduct.ItemGroup;
import com.swam.commons.mongodb.AbstractProductDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.ProductType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class ProductTypeDTO extends AbstractProductDTO<ProductType> {

    @PersistenceCreator
    @JsonCreator
    public ProductTypeDTO(@JsonProperty("name") String name,
            @JsonProperty("quantityProduced") Integer quantityProduced, @JsonProperty("pdf") StochasticTime pdf,
            @JsonProperty("itemGroup") ItemGroup itemGroup) {
        super(name, quantityProduced, pdf, itemGroup);
    }

    public ProductTypeDTO(ProductType product) {
        super(product);
    }

    @Override
    public ProductType createProduct() {
        return new ProductType();
    }

}
