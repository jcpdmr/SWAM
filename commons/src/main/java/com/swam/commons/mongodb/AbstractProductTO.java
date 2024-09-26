package com.swam.commons.mongodb;

import java.util.Objects;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qesm.workflow.AbstractProduct;
import com.qesm.workflow.AbstractProduct.ItemGroup;
import com.swam.commons.intercommunication.ProcessingMessageException;
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
public abstract class AbstractProductTO<V extends AbstractProduct> implements MongodbTO<V> {
    private final String name;
    private final Integer quantityProduced;
    @JsonDeserialize(using = StochasticTimeDeserializer.class)
    @JsonSerialize(using = StochasticTimeSerializer.class)
    private final transient StochasticTime pdf;
    private final ItemGroup itemGroup;

    protected AbstractProductTO(V product) {
        this.name = product.getName();
        if (product.isProcessed()) {
            this.quantityProduced = product.getQuantityProduced();
            this.pdf = product.getPdf();
            this.itemGroup = ItemGroup.PROCESSED;
        } else {
            this.quantityProduced = null;
            this.pdf = null;
            this.itemGroup = ItemGroup.RAW_MATERIAL;
        }
    }

    /**
     * Delegate the responsability to create the right type of object to the class
     * that inherit from AbstractProductTO.
     * 
     * @return Object of a type that extends AbstractProductTO
     */
    protected abstract V createProcessedProduct(String name, Integer quantityProduced, StochasticTime pdf);

    protected abstract V createRawProduct(String name);

    /**
     * 
     * @return
     */
    private V toProduct() {
        if (itemGroup == ItemGroup.PROCESSED) {
            return createProcessedProduct(name, quantityProduced, pdf);
        } else {
            return createRawProduct(name);
        }
    }

    @Override
    public V convertAndValidate() throws ProcessingMessageException {
        try {
            return toProduct();
        } catch (Exception e) {
            throw new ProcessingMessageException(e.getMessage(), "Validation error: cannot convert TO to Product",
                    400);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        AbstractProductTO<V> abstractProductTOToCompare = uncheckedCast(obj);

        if (!name.equals(abstractProductTOToCompare.name)) {
            return false;
        }

        if (!itemGroup.equals(abstractProductTOToCompare.itemGroup)) {
            return false;
        }

        if (itemGroup.equals(ItemGroup.PROCESSED)) {
            if (!quantityProduced.equals(abstractProductTOToCompare.quantityProduced)) {
                return false;
            }
            if (!AbstractProduct.arePdfEquals(pdf, abstractProductTOToCompare.pdf)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        if (itemGroup.equals(ItemGroup.PROCESSED)) {
            return Objects.hash(name, itemGroup, quantityProduced, pdf);
        } else {
            return Objects.hash(name, itemGroup);
        }
    }
}
