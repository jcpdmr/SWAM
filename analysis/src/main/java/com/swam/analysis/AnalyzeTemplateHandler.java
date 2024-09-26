package com.swam.analysis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qesm.ProductTemplate;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateTO;

@Service
public class AnalyzeTemplateHandler extends AbstractAnalyzeHandler<ProductTemplate> {
    public AnalyzeTemplateHandler() {
        super(List.of(TargetMessageHandler.ANALYZE_TEMPLATE), WorkflowTemplateTO.class, ProductTemplate.class);
    }
}
