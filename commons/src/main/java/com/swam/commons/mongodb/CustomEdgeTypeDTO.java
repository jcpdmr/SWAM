package com.swam.commons.mongodb;

import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import com.qesm.AbstractProduct;
import com.qesm.CustomEdge;
import com.qesm.ProductType;

import lombok.Getter;

@Document
@Getter
public class CustomEdgeTypeDTO extends AbstractCustomEdgeDTO {

    @PersistenceCreator
    public CustomEdgeTypeDTO(String source, String target, Integer quantityRequired) {
        super(source, target, quantityRequired);

    }

    public CustomEdgeTypeDTO(CustomEdge customEdge) {
        super(((AbstractProduct) customEdge.getSource()).getUuid().toString(),
                ((AbstractProduct) customEdge.getTarget()).getUuid().toString(), customEdge.getQuantityRequired());
    }

    @Override
    public CustomEdge toEdge() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toProduct'");
    }

}
