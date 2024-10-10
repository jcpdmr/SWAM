package com.swam.operation;

import org.springframework.stereotype.Service;

import com.qesm.workflow.ProductInstance;
import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceEntity;
import com.swam.commons.mongodb.instance.WorkflowInstanceEntityRepository;

@Service
public class CRUDWorkflowInstanceHandler extends
        AbstractCRUDWorkflowHandler<WorkflowInstanceEntity, ProductInstance> {

    public CRUDWorkflowInstanceHandler(WorkflowInstanceEntityRepository workflowInstanceRepository) {
        super(workflowInstanceRepository, WorkflowInstanceEntity.class);
    }

}
