package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.ProductTemplate;
import com.swam.commons.messageHandlers.AbstractForwardWorkflowHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateTO;
import com.swam.commons.mongodb.template.WorkflowTemplateTORepository;

@Service
public class ForwardWorkflowTemplateHandler
        extends AbstractForwardWorkflowHandler<WorkflowTemplateTO, ProductTemplate> {

    public ForwardWorkflowTemplateHandler(WorkflowTemplateTORepository workflowTemplateRepository) {
        super(workflowTemplateRepository);
    }

}
