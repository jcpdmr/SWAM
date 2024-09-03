package com.swam.operation;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractForwardWorkflowHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTORepository;

@Service
public class ForwardWorkflowInstanceHandler extends AbstractForwardWorkflowHandler<WorkflowInstanceDTO> {

    public ForwardWorkflowInstanceHandler(WorkflowInstanceDTORepository workflowInstanceRepository) {
        super(workflowInstanceRepository);
    }

}
