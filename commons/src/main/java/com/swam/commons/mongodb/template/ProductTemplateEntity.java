package com.swam.commons.mongodb.template;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;

import com.qesm.workflow.AbstractProduct.ItemGroup;
import com.swam.commons.mongodb.AbstractProductEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qesm.workflow.ProductTemplate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class ProductTemplateEntity extends AbstractProductEntity<ProductTemplate> {

    @PersistenceCreator
    @JsonCreator
    public ProductTemplateEntity(@JsonProperty("name") String name,
            @JsonProperty("quantityProduced") Integer quantityProduced, @JsonProperty("pdf") StochasticTime pdf,
            @JsonProperty("itemGroup") ItemGroup itemGroup) {
        super(name, quantityProduced, pdf, itemGroup);
    }

    public ProductTemplateEntity(ProductTemplate product) {
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
