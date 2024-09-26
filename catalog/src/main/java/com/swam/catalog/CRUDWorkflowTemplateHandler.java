package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.ProductTemplate;
import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateTO;
import com.swam.commons.mongodb.template.WorkflowTemplateTORepository;

@Service
public class CRUDWorkflowTemplateHandler extends
        AbstractCRUDWorkflowHandler<WorkflowTemplateTO, ProductTemplate> {

    public CRUDWorkflowTemplateHandler(WorkflowTemplateTORepository workflowTemplateRepository) {
        super(workflowTemplateRepository, WorkflowTemplateTO.class);
    }

}
