package com.swam.commons;

import java.util.UUID;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;

import com.qesm.ProductIstance;

public class ProductIstanceDTO extends AbstractProductDTO {

    @PersistenceCreator
    public ProductIstanceDTO(String name, String id, int quantityProduced, StochasticTime pdf, ItemGroup itemGroup,
            Boolean isType) {
        super(name, id, quantityProduced, pdf, itemGroup, isType);
    }

    public ProductIstanceDTO(ProductIstance product) {
        super(product);
    }

    @Override
    public ProductIstance toProduct() {
        if (getItemGroup() == ItemGroup.PROCESSED) {
            return new ProductIstance(getName(), UUID.fromString(getId()), getQuantityProduced(), getPdf());
        } else {
            return new ProductIstance(getName(), UUID.fromString(getId()));
        }
    }

}
