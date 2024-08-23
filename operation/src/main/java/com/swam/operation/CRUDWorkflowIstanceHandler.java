package com.swam.operation;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.istance.WorkflowIstanceDTO;
import com.swam.commons.mongodb.istance.WorkflowIstanceDTORepository;

@Service
public class CRUDWorkflowIstanceHandler extends
        AbstractCRUDWorkflowHandler<WorkflowIstanceDTO> {

    public CRUDWorkflowIstanceHandler(WorkflowIstanceDTORepository workflowIstanceRepository) {
        super(workflowIstanceRepository, WorkflowIstanceDTO.class);
    }

}
