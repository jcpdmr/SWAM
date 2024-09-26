package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.ProductTemplate;
import com.swam.commons.messageHandlers.AbstractCRUDProductHandler;
import com.swam.commons.mongodb.template.ProductTemplateTO;
import com.swam.commons.mongodb.template.WorkflowTemplateTO;
import com.swam.commons.mongodb.template.WorkflowTemplateTORepository;

@Service
public class CRUDProductTemplateHandler extends AbstractCRUDProductHandler<WorkflowTemplateTO, ProductTemplate> {

    public CRUDProductTemplateHandler(WorkflowTemplateTORepository workflowTemplateRepository) {
        super(workflowTemplateRepository, ProductTemplateTO.class);
    }

}
