package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.swam.commons.messageHandlers.AbstractForwardWorkflowHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateDTO;
import com.swam.commons.mongodb.template.WorkflowTemplateDTORepository;

@Service
public class ForwardWorkflowTemplateHandler extends AbstractForwardWorkflowHandler<WorkflowTemplateDTO> {

    public ForwardWorkflowTemplateHandler(WorkflowTemplateDTORepository workflowTemplateRepository) {
        super(workflowTemplateRepository);
    }

}