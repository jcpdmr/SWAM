package com.swam.commons.mongodb.instance;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.AbstractProduct.ItemGroup;
import com.qesm.ProductInstance;
import com.swam.commons.mongodb.AbstractProductDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ProductInstanceDTO extends AbstractProductDTO<ProductInstance> {

    @PersistenceCreator
    @JsonCreator
    public ProductInstanceDTO(@JsonProperty("name") String name,
            @JsonProperty("quantityProduced") Integer quantityProduced, @JsonProperty("pdf") StochasticTime pdf,
            @JsonProperty("itemGroup") ItemGroup itemGroup) {
        super(name, quantityProduced, pdf, itemGroup);
    }

    public ProductInstanceDTO(ProductInstance product) {
        super(product);
    }

    @Override
    protected ProductInstance createProcessedProduct(String name, Integer quantityProduced, StochasticTime pdf) {
        return new ProductInstance(name, quantityProduced, pdf);
    }

    @Override
    protected ProductInstance createRawProduct(String name) {
        return new ProductInstance(name);
    }

}
