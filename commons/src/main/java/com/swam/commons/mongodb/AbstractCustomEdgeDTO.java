package com.swam.commons.mongodb;

import java.util.Objects;

import com.qesm.AbstractProduct;
import com.qesm.CustomEdge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public abstract class AbstractCustomEdgeDTO {

    protected final String sourceName;
    protected final String targetName;
    protected final Integer quantityRequired;

    protected AbstractCustomEdgeDTO(CustomEdge customEdge) {
        this.sourceName = ((AbstractProduct) customEdge.getSource()).getName();
        this.targetName = ((AbstractProduct) customEdge.getTarget()).getName();
        this.quantityRequired = customEdge.getQuantityRequired();
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

        AbstractCustomEdgeDTO customEdgeToCompare = (AbstractCustomEdgeDTO) obj;
        if (sourceName == null) {
            if (customEdgeToCompare.sourceName != null)
                return false;
        } else if (!sourceName.equals(customEdgeToCompare.sourceName))
            return false;
        if (targetName == null) {
            if (customEdgeToCompare.targetName != null)
                return false;
        } else if (!targetName.equals(customEdgeToCompare.targetName))
            return false;
        if (quantityRequired == null) {
            if (customEdgeToCompare.quantityRequired != null)
                return false;
        } else if (!quantityRequired.equals(customEdgeToCompare.quantityRequired))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceName, targetName, quantityRequired);
    }
}
