package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.type.WorkflowTypeDTO;
import com.swam.commons.mongodb.type.WorkflowTypeDTORepository;

@Service
public class CRUDWorkflowTypeHandler extends
        AbstractCRUDWorkflowHandler<WorkflowTypeDTO> {

    public CRUDWorkflowTypeHandler(WorkflowTypeDTORepository workflowTypeRepository) {
        super(workflowTypeRepository, WorkflowTypeDTO.class);
    }

}
