package com.swam.operation;

import org.springframework.stereotype.Service;

import com.swam.commons.mongodb.istance.WorkflowIstanceDTO;
import com.swam.commons.mongodb.istance.WorkflowIstanceRepository;
import com.swam.commons.task.AbstractCRUDWorkflowTask;

@Service
public class CRUDWorkflowIstanceTask extends
        AbstractCRUDWorkflowTask<WorkflowIstanceRepository, WorkflowIstanceDTO> {

    public CRUDWorkflowIstanceTask(WorkflowIstanceRepository workflowIstanceRepository) {
        super(workflowIstanceRepository);
    }

}
