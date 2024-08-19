package com.swam.catalog;

import java.util.UUID;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.ProductType;
import com.swam.commons.AbstractProductDTO;

import lombok.Getter;

@Document
@Getter
public class ProductTypeDTO extends AbstractProductDTO {

    @PersistenceCreator
    public ProductTypeDTO(String name, String id, int quantityProduced, StochasticTime pdf, ItemGroup itemGroup,
            Boolean isType) {
        super(name, id, quantityProduced, pdf, itemGroup, isType);
    }

    public ProductTypeDTO(ProductType product) {
        super(product);
    }

    @Override
    public ProductType toProduct() {
        if (getItemGroup() == ItemGroup.PROCESSED) {
            return new ProductType(getName(), UUID.fromString(getId()), getQuantityProduced(), getPdf());
        } else {
            return new ProductType(getName(), UUID.fromString(getId()));
        }
    }

}
