package com.swam.commons.mongodb;

import java.util.UUID;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qesm.AbstractProduct;
import com.qesm.AbstractProduct.ItemGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractProductDTO<V extends AbstractProduct> {
    private String name;
    private @Id String id;
    private Integer quantityProduced;
    private transient StochasticTime pdf;
    private ItemGroup itemGroup;

    protected AbstractProductDTO(V product) {
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

    protected abstract V createProduct(UUID uuid);

    public V toProduct() {
        V product = createProduct(UUID.fromString(id));

        // Common fields
        product.setName(name);
        product.setItemGroup(itemGroup);

        if (itemGroup == ItemGroup.PROCESSED) {
            product.setQuantityProduced(quantityProduced);
            product.setPdf(pdf);
        }

        return product;
    }

}
