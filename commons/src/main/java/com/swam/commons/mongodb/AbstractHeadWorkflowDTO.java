package com.swam.commons.mongodb;

import java.util.List;
import java.util.ArrayList;

import java.util.Set;
import java.util.Map.Entry;

import com.qesm.AbstractProduct;
import com.qesm.AbstractWorkflow;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AbstractHeadWorkflowDTO<P extends AbstractProduct> extends AbstractBaseWorkflowDTO<P> {

    protected final List<AbstractBaseWorkflowDTO<P>> subWorkflowDTOList;

    protected AbstractHeadWorkflowDTO(String id, Set<? extends AbstractProductDTO<P>> vertexSet,
            Set<? extends AbstractCustomEdgeDTO> edgeSet, List<AbstractBaseWorkflowDTO<P>> subWorkflowDTOList) {
        super(id, vertexSet, edgeSet);
        this.subWorkflowDTOList = subWorkflowDTOList;

    }

    protected AbstractHeadWorkflowDTO(AbstractWorkflow<P> workflow) {

        super(workflow);

        // Create subWorkflowDTOList only for the top tier workflowDTO (to avoid
        // recursive repetition of data)
        if (workflow.isTopTier()) {
            this.subWorkflowDTOList = new ArrayList<>();
            for (Entry<P, AbstractWorkflow<P>> entry : workflow.getProductToSubWorkflowMap().entrySet()) {
                subWorkflowDTOList.add(
                        createWorkflowDTO(entry.getValue(), entry.getKey().getUuid().toString()));
            }
        } else {
            this.subWorkflowDTOList = null;
        }

    }

}
