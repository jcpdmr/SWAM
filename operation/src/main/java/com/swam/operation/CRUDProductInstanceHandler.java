package com.swam.operation;

import org.springframework.stereotype.Service;

import com.qesm.workflow.ProductInstance;
import com.swam.commons.messageHandlers.AbstractCRUDProductHandler;
import com.swam.commons.mongodb.instance.ProductInstanceEntity;
import com.swam.commons.mongodb.instance.WorkflowInstanceEntity;
import com.swam.commons.mongodb.instance.WorkflowInstanceEntityRepository;

@Service
public class CRUDProductInstanceHandler extends AbstractCRUDProductHandler<WorkflowInstanceEntity, ProductInstance> {

    public CRUDProductInstanceHandler(WorkflowInstanceEntityRepository workflowInstanceRepository) {
        super(workflowInstanceRepository, ProductInstanceEntity.class);
    }

}
