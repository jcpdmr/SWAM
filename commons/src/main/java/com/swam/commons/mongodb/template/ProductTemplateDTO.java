package com.swam.commons.mongodb.template;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;

import com.qesm.AbstractProduct.ItemGroup;
import com.swam.commons.mongodb.AbstractProductDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.ProductTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class ProductTemplateDTO extends AbstractProductDTO<ProductTemplate> {

    @PersistenceCreator
    @JsonCreator
    public ProductTemplateDTO(@JsonProperty("name") String name,
            @JsonProperty("quantityProduced") Integer quantityProduced, @JsonProperty("pdf") StochasticTime pdf,
            @JsonProperty("itemGroup") ItemGroup itemGroup) {
        super(name, quantityProduced, pdf, itemGroup);
    }

    public ProductTemplateDTO(ProductTemplate product) {
        super(product);
    }

    @Override
    protected ProductTemplate createProcessedProduct(String name, Integer quantityProduced, StochasticTime pdf) {
        return new ProductTemplate(name, quantityProduced, pdf);
    }

    @Override
    protected ProductTemplate createRawProduct(String name) {
        return new ProductTemplate(name);
    }

}
