package com.swam.operation;

import org.springframework.stereotype.Service;

import com.qesm.ProductInstance;
import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTORepository;

@Service
public class CRUDWorkflowInstanceHandler extends
        AbstractCRUDWorkflowHandler<WorkflowInstanceDTO, ProductInstance> {

    public CRUDWorkflowInstanceHandler(WorkflowInstanceDTORepository workflowInstanceRepository) {
        super(workflowInstanceRepository, WorkflowInstanceDTO.class);
    }

}
