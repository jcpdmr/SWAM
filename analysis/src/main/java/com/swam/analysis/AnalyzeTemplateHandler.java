package com.swam.analysis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.template.WorkflowTemplateDTO;

@Service
public class AnalyzeTemplateHandler extends AbstractAnalyzeHandler {
    public AnalyzeTemplateHandler() {
        super(WorkflowTemplateDTO.class);
    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.ANALYZE_TEMPLATE);
    }
}
