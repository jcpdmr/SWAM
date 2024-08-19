package com.swam.catalog;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.CustomEdge;
import com.qesm.ProductType;
import com.swam.commons.AbstractCustomEdgeDTO;

import lombok.Getter;

@Document
@Getter
public class CustomEdgeTypeDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeTypeDTO(ProductTypeDTO source, ProductTypeDTO target, Integer quantityRequired) {
        super(source, target, quantityRequired);

    }

    public CustomEdgeTypeDTO(CustomEdge customEdge) {
        super(new ProductTypeDTO((ProductType) customEdge.getSource()),
                new ProductTypeDTO((ProductType) customEdge.getTarget()), customEdge.getQuantityRequired());
    }

    @Override
    public CustomEdge toEdge() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toProduct'");
    }

}
