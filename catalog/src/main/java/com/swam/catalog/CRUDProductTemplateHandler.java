package com.swam.catalog;

import org.springframework.stereotype.Service;

import com.qesm.ProductTemplate;
import com.swam.commons.messageHandlers.AbstractCRUDProductHandler;
import com.swam.commons.mongodb.template.ProductTemplateDTO;
import com.swam.commons.mongodb.template.WorkflowTemplateDTO;
import com.swam.commons.mongodb.template.WorkflowTemplateDTORepository;

@Service
public class CRUDProductTemplateHandler extends AbstractCRUDProductHandler<WorkflowTemplateDTO, ProductTemplate> {

    public CRUDProductTemplateHandler(WorkflowTemplateDTORepository workflowTemplateRepository) {
        super(workflowTemplateRepository, ProductTemplateDTO.class);
    }

}
