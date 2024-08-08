package com.swam.multimodule.commons;

import java.util.Optional;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.AbstractProduct;
import com.qesm.ProductIstance;
import com.qesm.ProductType;

@Document
public class ProductDTO <T extends AbstractProduct> {
    private final String name;
    private final @Id String id;
    private final int quantityProduced;
    private final transient StochasticTime pdf;
    // private final Class<?> objectClass;

    // TODO: is it possible to remove these duplicated lines of code?
    enum ItemGroup {
        RAW_MATERIAL,
        PROCESSED
    }

    private final ItemGroup itemGroup;

    @PersistenceCreator
    public ProductDTO(String name, String id, int quantityProduced, StochasticTime pdf, ItemGroup itemGroup) {
        this.name = name;
        this.quantityProduced = quantityProduced;
        this.pdf = pdf;
        this.id = id;
        this.itemGroup = itemGroup;
        // this.objectClass = objectClass;
    }

    public ProductDTO(T product) {
        this.name = product.getName();
        this.id = product.getUuid().toString();
        // this.objectClass = product.getClass();

        if (product.isProcessed()) {
            this.quantityProduced = product.getQuantityProduced().get();
            this.pdf = product.getPdf().get();
            this.itemGroup = ItemGroup.PROCESSED;
        } else {
            this.quantityProduced = 0;
            this.pdf = null;
            this.itemGroup = ItemGroup.RAW_MATERIAL;
        }

    }

    public Optional<T> toProduct() {
        if (itemGroup == ItemGroup.PROCESSED) {
            // if (objectClass == ProductType.class){
                @SuppressWarnings("unchecked")
                T product = (T) new ProductType(name, quantityProduced, pdf);
                return Optional.of(product);
            // }
            // else if (objectClass == ProductIstance.class){
            //     ProductType myProductTypeExample = new ProductType("myProductTypeExample", 1,  null);
            //     @SuppressWarnings("unchecked")
            //     T product = (T) new ProductIstance(myProductTypeExample);
            //     return Optional.of(product);
            // }
            // else{
            //     return Optional.empty();
            // }
        } else {
            // if (objectClass == ProductType.class){
                @SuppressWarnings("unchecked")
                T product = (T) new ProductType(name);
                return Optional.of(product);
            // }
            // else if (objectClass == ProductIstance.class){
            //     ProductType myProductTypeExample = new ProductType("myProductTypeExample");
            //     @SuppressWarnings("unchecked")
            //     T product = (T) new ProductIstance(myProductTypeExample);
            //     return Optional.of(product);
            // }
            // else{
            //     return Optional.empty();
            // }
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
}
