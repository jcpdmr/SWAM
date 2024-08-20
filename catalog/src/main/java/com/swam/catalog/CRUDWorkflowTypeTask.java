package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.swam.commons.mongodb.type.WorkflowTypeDTO;
import com.swam.commons.mongodb.type.WorkflowTypeRepository;
import com.swam.commons.task.AbstractCRUDWorkflowTask;

@Service
public class CRUDWorkflowTypeTask extends
        AbstractCRUDWorkflowTask<WorkflowTypeRepository, WorkflowTypeDTO> {

    public CRUDWorkflowTypeTask(WorkflowTypeRepository workflowTypeRepository) {
        super(workflowTypeRepository);
    }

}
