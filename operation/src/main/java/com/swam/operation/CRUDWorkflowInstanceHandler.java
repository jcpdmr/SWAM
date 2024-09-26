package com.swam.operation;

import org.springframework.stereotype.Service;

import com.qesm.ProductInstance;
import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceTORepository;

@Service
public class CRUDWorkflowInstanceHandler extends
        AbstractCRUDWorkflowHandler<WorkflowInstanceTO, ProductInstance> {

    public CRUDWorkflowInstanceHandler(WorkflowInstanceTORepository workflowInstanceRepository) {
        super(workflowInstanceRepository, WorkflowInstanceTO.class);
    }

}
