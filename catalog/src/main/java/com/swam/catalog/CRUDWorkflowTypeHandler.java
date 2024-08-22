package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.type.HeadWorkflowTypeDTO;
import com.swam.commons.mongodb.type.WorkflowTypeDTORepository;

@Service
public class CRUDWorkflowTypeHandler extends
        AbstractCRUDWorkflowHandler<HeadWorkflowTypeDTO> {

    public CRUDWorkflowTypeHandler(WorkflowTypeDTORepository workflowTypeRepository) {
        super(workflowTypeRepository, HeadWorkflowTypeDTO.class);
    }

}
