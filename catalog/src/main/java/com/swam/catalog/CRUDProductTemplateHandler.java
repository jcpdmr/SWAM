package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.workflow.ProductTemplate;
import com.swam.commons.messageHandlers.AbstractCRUDProductHandler;
import com.swam.commons.mongodb.template.ProductTemplateEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntityRepository;

@Service
public class CRUDProductTemplateHandler extends AbstractCRUDProductHandler<WorkflowTemplateEntity, ProductTemplate> {

    public CRUDProductTemplateHandler(WorkflowTemplateEntityRepository workflowTemplateRepository) {
        super(workflowTemplateRepository, ProductTemplateEntity.class);
    }

}
