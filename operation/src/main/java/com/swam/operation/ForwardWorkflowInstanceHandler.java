package com.swam.operation;

import org.springframework.stereotype.Service;

import com.qesm.workflow.ProductInstance;
import com.swam.commons.messageHandlers.AbstractForwardWorkflowHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceEntity;
import com.swam.commons.mongodb.instance.WorkflowInstanceEntityRepository;

@Service
public class ForwardWorkflowInstanceHandler
        extends AbstractForwardWorkflowHandler<WorkflowInstanceEntity, ProductInstance> {

    public ForwardWorkflowInstanceHandler(WorkflowInstanceEntityRepository workflowInstanceRepository) {
        super(workflowInstanceRepository);
    }

}
