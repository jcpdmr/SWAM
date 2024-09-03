package com.swam.analysis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTO;

@Service
public class AnalyzeInstanceHandler extends AbstractAnalyzeHandler {
    public AnalyzeInstanceHandler() {
        super(WorkflowInstanceDTO.class);
    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.ANALYZE_INSTANCE);
    }
}
