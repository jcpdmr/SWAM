package com.swam.operation;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTORepository;

@Service
public class CRUDWorkflowInstanceHandler extends
        AbstractCRUDWorkflowHandler<WorkflowInstanceDTO> {

    public CRUDWorkflowInstanceHandler(WorkflowInstanceDTORepository workflowIstanceRepository) {
        super(workflowIstanceRepository, WorkflowInstanceDTO.class);
    }

}
