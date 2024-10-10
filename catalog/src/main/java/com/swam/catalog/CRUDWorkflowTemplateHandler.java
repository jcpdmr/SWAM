package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.workflow.ProductTemplate;
import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntityRepository;

@Service
public class CRUDWorkflowTemplateHandler extends
        AbstractCRUDWorkflowHandler<WorkflowTemplateEntity, ProductTemplate> {

    public CRUDWorkflowTemplateHandler(WorkflowTemplateEntityRepository workflowTemplateRepository) {
        super(workflowTemplateRepository, WorkflowTemplateEntity.class);
    }

}
