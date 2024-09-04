package com.swam.operation;

import org.springframework.stereotype.Service;

import com.qesm.ProductInstance;
import com.swam.commons.messageHandlers.AbstractCRUDProductHandler;
import com.swam.commons.mongodb.instance.ProductInstanceDTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTO;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTORepository;

@Service
public class CRUDProductInstanceHandler extends AbstractCRUDProductHandler<WorkflowInstanceDTO, ProductInstance> {

    public CRUDProductInstanceHandler(WorkflowInstanceDTORepository workflowInstanceRepository) {
        super(workflowInstanceRepository, ProductInstanceDTO.class);
    }

}
