package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractCRUDProductHandler;
import com.swam.commons.mongodb.type.WorkflowTypeDTO;
import com.swam.commons.mongodb.type.WorkflowTypeDTORepository;

@Service
public class CRUDProductTypeHandler extends AbstractCRUDProductHandler<WorkflowTypeDTO> {

    public CRUDProductTypeHandler(WorkflowTypeDTORepository workflowTypeRepository) {
        super(workflowTypeRepository, WorkflowTypeDTO.class);
    }

}
