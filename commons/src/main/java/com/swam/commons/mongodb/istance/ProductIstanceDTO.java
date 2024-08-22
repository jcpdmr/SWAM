package com.swam.commons.mongodb.istance;

import java.util.UUID;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.AbstractProduct.ItemGroup;
import com.qesm.ProductIstance;
import com.swam.commons.mongodb.AbstractProductDTO;

@Document
public class ProductIstanceDTO extends AbstractProductDTO<ProductIstance> {

    @PersistenceCreator
    public ProductIstanceDTO(String name, String id, Integer quantityProduced, StochasticTime pdf,
            ItemGroup itemGroup) {
        super(name, id, quantityProduced, pdf, itemGroup);
    }

    public ProductIstanceDTO(ProductIstance product) {
        super(product);
    }

    @Override
    public ProductIstance createProduct(UUID uuid) {
        return new ProductIstance(uuid);
    }

}
