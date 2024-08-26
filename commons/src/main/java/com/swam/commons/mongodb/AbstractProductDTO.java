package com.swam.commons.mongodb;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qesm.AbstractProduct;
import com.qesm.AbstractProduct.ItemGroup;
import com.swam.commons.intercommunication.StochasticTimeDeserializer;
import com.swam.commons.intercommunication.StochasticTimeSerializer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractProductDTO<V extends AbstractProduct> implements MongodbDTO {
    private final String name;
    private final Integer quantityProduced;
    @JsonDeserialize(using = StochasticTimeDeserializer.class)
    @JsonSerialize(using = StochasticTimeSerializer.class)
    private final transient StochasticTime pdf;
    private final ItemGroup itemGroup;

    protected AbstractProductDTO(V product) {
        this.name = product.getName();
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
     * that inherit from AbstractProductDTO.
     * 
     * @return Object of a type that extends AbstractProductDTO
     */
    protected abstract V createProduct();

    /**
     * 
     * @return
     */
    public V toProduct() {
        V product = createProduct();

        // Set common fields
        product.setName(name);
        product.setItemGroup(itemGroup);

        if (itemGroup == ItemGroup.PROCESSED) {
            product.setQuantityProduced(quantityProduced);
            product.setPdf(pdf);
        }

        return product;
    }

    @Override
    public Boolean isValid() {
        try {
            V abstractProduct = toProduct();
        } catch (Exception e) {
            System.err.println("Validation error: cannot convert DTO to Product");
            System.err.println(e.getMessage());
            ;
            return false;
        }
        return true;
    }
}
