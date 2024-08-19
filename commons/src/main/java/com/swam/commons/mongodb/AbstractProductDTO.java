package com.swam.commons.mongodb;

import java.util.UUID;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qesm.AbstractProduct;
import com.qesm.AbstractProduct.ItemGroup;

import lombok.AllArgsConstructor;

import lombok.Setter;

@AllArgsConstructor(onConstructor = @__({ @PersistenceCreator }))
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document
public abstract class AbstractProductDTO<T extends AbstractProduct> {
    private String name;
    private @Id String id;
    private Integer quantityProduced;
    private transient StochasticTime pdf;
    private ItemGroup itemGroup;

    public AbstractProductDTO(T product) {
        this.name = product.getName();
        this.id = product.getUuid().toString();
        if (product.isProcessed()) {
            this.quantityProduced = product.getQuantityProduced().get();
            this.pdf = product.getPdf().get();
            this.itemGroup = ItemGroup.PROCESSED;
        } else {
            this.quantityProduced = null;
            this.pdf = null;
            this.itemGroup = ItemGroup.RAW_MATERIAL;
        }
    }

    public abstract T createProduct();

    public T toProduct() {
        T product = createProduct();

        // Common fields
        product.setName(name);
        product.setUuid(UUID.fromString(id));
        product.setItemGroup(itemGroup);

        if (itemGroup == ItemGroup.PROCESSED) {
            product.setQuantityProduced(quantityProduced);
            product.setPdf(pdf);
        }

        return product;
    }

}
