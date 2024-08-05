package com.swam.catalog;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.ProductType;

@Document
public class ProductTypeDTO {

    private final String name;
    private final @Id String id;
    private final int quantityProduced;
    private final transient StochasticTime pdf;

    enum ItemGroup {
        RAW_MATERIAL,
        PROCESSED
    }

    private final ItemGroup itemGroup;

    @PersistenceCreator
    public ProductTypeDTO(String name, String id, int quantityProduced, StochasticTime pdf, ItemGroup itemGroup) {
        this.name = name;
        this.quantityProduced = quantityProduced;
        this.pdf = pdf;
        this.id = id;
        this.itemGroup = itemGroup;
    }

    public ProductTypeDTO(ProductType productType) {
        this.name = productType.getName();
        this.id = productType.getUuid().toString();

        if (productType.isProcessed()) {
            this.quantityProduced = productType.getQuantityProduced().get();
            this.pdf = productType.getPdf().get();
            this.itemGroup = ItemGroup.PROCESSED;
        } else {
            this.quantityProduced = 0;
            this.pdf = null;
            this.itemGroup = ItemGroup.RAW_MATERIAL;
        }

    }

    public ProductType toProductType() {
        if (itemGroup == ItemGroup.PROCESSED) {
            return new ProductType(name, quantityProduced, pdf);
        } else {
            return new ProductType(name);
        }

    }

    @Override
    public String toString() {
        String info = name + " " + itemGroup;
        if (itemGroup == ItemGroup.PROCESSED) {
            info += " " + quantityProduced + " " + pdf;
        }
        return info;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getQuantityProduced() {
        return quantityProduced;
    }

    public StochasticTime getPdf() {
        return pdf;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

}
