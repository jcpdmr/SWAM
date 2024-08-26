package com.swam.commons.mongodb.istance;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.AbstractProduct.ItemGroup;
import com.qesm.ProductIstance;
import com.swam.commons.mongodb.AbstractProductDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ProductIstanceDTO extends AbstractProductDTO<ProductIstance> {

    @PersistenceCreator
    @JsonCreator
    public ProductIstanceDTO(@JsonProperty("name") String name,
            @JsonProperty("quantityProduced") Integer quantityProduced, @JsonProperty("pdf") StochasticTime pdf,
            @JsonProperty("itemGroup") ItemGroup itemGroup) {
        super(name, quantityProduced, pdf, itemGroup);
    }

    public ProductIstanceDTO(ProductIstance product) {
        super(product);
    }

    @Override
    public ProductIstance createProduct() {
        return new ProductIstance();
    }

}
