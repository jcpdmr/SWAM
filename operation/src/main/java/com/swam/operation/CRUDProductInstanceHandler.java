package com.swam.operation;

import org.springframework.stereotype.Service;

import com.qesm.ProductInstance;
import com.swam.commons.messageHandlers.AbstractCRUDProductHandler;
import com.swam.commons.mongodb.instance.ProductInstanceTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceTORepository;

@Service
public class CRUDProductInstanceHandler extends AbstractCRUDProductHandler<WorkflowInstanceTO, ProductInstance> {

    public CRUDProductInstanceHandler(WorkflowInstanceTORepository workflowInstanceRepository) {
        super(workflowInstanceRepository, ProductInstanceTO.class);
    }

}
