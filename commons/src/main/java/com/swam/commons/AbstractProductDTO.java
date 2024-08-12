package com.swam.commons;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.Id;

import com.qesm.AbstractProduct;
import com.qesm.ProductType;

import lombok.Getter;

@Getter
public abstract class AbstractProductDTO {
    private final String name;
    private final @Id String id;
    private final int quantityProduced;
    private final transient StochasticTime pdf;
    private final Boolean isType;

    // TODO: is it possible to remove these duplicated lines of code?
    enum ItemGroup {
        RAW_MATERIAL,
        PROCESSED
    }

    private final ItemGroup itemGroup;

    public AbstractProductDTO(String name, String id, int quantityProduced, StochasticTime pdf, ItemGroup itemGroup,
            Boolean isType) {
        this.name = name;
        this.quantityProduced = quantityProduced;
        this.pdf = pdf;
        this.id = id;
        this.itemGroup = itemGroup;
        this.isType = isType;
    }

    public AbstractProductDTO(AbstractProduct product) {
        this.name = product.getName();
        this.id = product.getUuid().toString();

        if (product.isProcessed()) {
            this.quantityProduced = product.getQuantityProduced().get();
            this.pdf = product.getPdf().get();
            this.itemGroup = ItemGroup.PROCESSED;
        } else {
            this.quantityProduced = 0;
            this.pdf = null;
            this.itemGroup = ItemGroup.RAW_MATERIAL;
        }

        if (product.getClass() == ProductType.class) {
            this.isType = true;
        } else {
            this.isType = false;
        }

    }

    public abstract AbstractProduct toProduct();

    @Override
    public String toString() {
        String info = name + " " + itemGroup;
        if (itemGroup == ItemGroup.PROCESSED) {
            info += " " + quantityProduced + " " + pdf;
        }
        return info;
    }
}
