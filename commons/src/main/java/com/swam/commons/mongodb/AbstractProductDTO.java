package com.swam.commons.mongodb;

import java.util.UUID;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qesm.AbstractProduct;
import com.qesm.AbstractProduct.ItemGroup;
import com.swam.commons.intercommunication.StochasticTimeDeserializer;
import com.swam.commons.intercommunication.StochasticTimeSerializer;

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
    private final String name;
    private final @Id String id;
    private final Integer quantityProduced;
    @JsonDeserialize(using = StochasticTimeDeserializer.class)
    @JsonSerialize(using = StochasticTimeSerializer.class)
    private final transient StochasticTime pdf;
    private final ItemGroup itemGroup;

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

    /**
     * Delegate the responsability to create the right type of object to the class
     * that inherit from AbstractProductDTO. The implementation also need to set
     * UUID of Product.
     * 
     * @param uuid UUID to be assigned to the Product object
     * @return Object of a type that extends AbstractProductDTO
     */
    protected abstract V createProduct(UUID uuid);

    /**
     * 
     * @return
     */
    public V toProduct() {
        V product = createProduct(UUID.fromString(id));

        // Set common fields
        product.setName(name);
        product.setItemGroup(itemGroup);

        if (itemGroup == ItemGroup.PROCESSED) {
            product.setQuantityProduced(quantityProduced);
            product.setPdf(pdf);
        }

        return product;
    }

}
