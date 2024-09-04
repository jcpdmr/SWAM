package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.ProductTemplate;
import com.swam.commons.messageHandlers.AbstractCRUDWorkflowHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateDTO;
import com.swam.commons.mongodb.template.WorkflowTemplateDTORepository;

@Service
public class CRUDWorkflowTemplateHandler extends
        AbstractCRUDWorkflowHandler<WorkflowTemplateDTO, ProductTemplate> {

    public CRUDWorkflowTemplateHandler(WorkflowTemplateDTORepository workflowTemplateRepository) {
        super(workflowTemplateRepository, WorkflowTemplateDTO.class);
    }

}
