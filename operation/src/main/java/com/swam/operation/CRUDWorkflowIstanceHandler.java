package com.swam.operation;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.istance.HeadWorkflowIstanceDTO;
import com.swam.commons.mongodb.istance.WorkflowIstanceDTORepository;

@Service
public class CRUDWorkflowIstanceHandler extends
        AbstractCRUDWorkflowHandler<HeadWorkflowIstanceDTO> {

    public CRUDWorkflowIstanceHandler(WorkflowIstanceDTORepository workflowIstanceRepository) {
        super(workflowIstanceRepository, HeadWorkflowIstanceDTO.class);
    }

}
