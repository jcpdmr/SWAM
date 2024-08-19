package com.swam.commons.mongodb;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.qesm.ProductIstance;

import lombok.Getter;

@Document
@Getter
public class CustomEdgeIstanceDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeIstanceDTO(ProductIstanceDTO source, ProductIstanceDTO target, Integer quantityRequired) {
        super(source, target, quantityRequired);

    }

    public CustomEdgeIstanceDTO(CustomEdge customEdge) {
        super(new ProductIstanceDTO((ProductIstance) customEdge.getSource()),
                new ProductIstanceDTO((ProductIstance) customEdge.getTarget()), customEdge.getQuantityRequired());
    }

    @Override
    public CustomEdge toEdge() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toProduct'");
    }

}