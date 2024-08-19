package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.ProductType;
import com.swam.commons.mongodb.CustomEdgeTypeDTO;
import com.swam.commons.mongodb.ProductTypeDTO;
import com.swam.commons.mongodb.WorkflowTypeDTO;
import com.swam.commons.mongodb.WorkflowTypeRepository;
import com.swam.commons.task.AbstractCRUDWorkflowTask;

@Service
public class CRUDWorkflowTypeTask extends
        AbstractCRUDWorkflowTask<ProductTypeDTO, CustomEdgeTypeDTO, ProductType, WorkflowTypeDTO, WorkflowTypeRepository> {

    public CRUDWorkflowTypeTask(WorkflowTypeRepository workflowTypeRepository) {
        super(workflowTypeRepository);
    }

}
