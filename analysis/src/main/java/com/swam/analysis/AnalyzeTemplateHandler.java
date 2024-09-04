package com.swam.analysis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qesm.ProductTemplate;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateDTO;

@Service
public class AnalyzeTemplateHandler extends AbstractAnalyzeHandler<ProductTemplate> {
    public AnalyzeTemplateHandler() {
        super(WorkflowTemplateDTO.class, ProductTemplate.class);
    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.ANALYZE_TEMPLATE);
    }
}
