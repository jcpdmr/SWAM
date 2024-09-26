package com.swam.operation;

import org.springframework.stereotype.Service;

import com.qesm.ProductInstance;
import com.swam.commons.messageHandlers.AbstractForwardWorkflowHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceTORepository;

@Service
public class ForwardWorkflowInstanceHandler
        extends AbstractForwardWorkflowHandler<WorkflowInstanceTO, ProductInstance> {

    public ForwardWorkflowInstanceHandler(WorkflowInstanceTORepository workflowInstanceRepository) {
        super(workflowInstanceRepository);
    }

}
