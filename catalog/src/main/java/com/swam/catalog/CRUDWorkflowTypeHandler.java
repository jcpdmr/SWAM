package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.type.WorkflowTypeDTO;
import com.swam.commons.mongodb.type.WorkflowTypeRepository;

@Service
public class CRUDWorkflowTypeHandler extends
        AbstractCRUDWorkflowHandler<WorkflowTypeRepository, WorkflowTypeDTO> {

    public CRUDWorkflowTypeHandler(WorkflowTypeRepository workflowTypeRepository) {
        super(workflowTypeRepository);
    }

}
