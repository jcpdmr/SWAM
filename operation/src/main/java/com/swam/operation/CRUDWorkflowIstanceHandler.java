package com.swam.operation;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.istance.WorkflowIstanceDTO;
import com.swam.commons.mongodb.istance.WorkflowIstanceRepository;

@Service
public class CRUDWorkflowIstanceHandler extends
        AbstractCRUDWorkflowHandler<WorkflowIstanceRepository, WorkflowIstanceDTO> {

    public CRUDWorkflowIstanceHandler(WorkflowIstanceRepository workflowIstanceRepository) {
        super(workflowIstanceRepository);
    }

}
