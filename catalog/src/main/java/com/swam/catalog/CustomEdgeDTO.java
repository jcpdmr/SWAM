package com.swam.catalog;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.qesm.ProductType;

@Document
public class CustomEdgeDTO {

    private @Id String id;
    private final ProductTypeDTO source;
    private final ProductTypeDTO target;
    private final Integer quantityRequired;

    @PersistenceCreator
    public CustomEdgeDTO(ProductTypeDTO source, ProductTypeDTO target, Integer quantityRequired) {
        this.source = source;
        this.target = target;
        this.quantityRequired = quantityRequired;
    }

    public CustomEdgeDTO(CustomEdge customEdge) {
        this.source = new ProductTypeDTO((ProductType) customEdge.getSource());
        this.target = new ProductTypeDTO((ProductType) customEdge.getTarget());
        this.quantityRequired = customEdge.getQuantityRequired();
    }

    @Override
    public String toString() {
        return "( " + source + " -> " + target + " )";
    }

    public ProductTypeDTO getSource() {
        return source;
    }

    public ProductTypeDTO getTarget() {
        return target;
    }

    public Integer getQuantityRequired() {
        return quantityRequired;
    }

}
