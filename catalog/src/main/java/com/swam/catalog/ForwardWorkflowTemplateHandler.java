package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.workflow.ProductTemplate;
import com.swam.commons.messageHandlers.AbstractForwardWorkflowHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateEntity;
import com.swam.commons.mongodb.template.WorkflowTemplateEntityRepository;

@Service
public class ForwardWorkflowTemplateHandler
        extends AbstractForwardWorkflowHandler<WorkflowTemplateEntity, ProductTemplate> {

    public ForwardWorkflowTemplateHandler(WorkflowTemplateEntityRepository workflowTemplateRepository) {
        super(workflowTemplateRepository);
    }

}
