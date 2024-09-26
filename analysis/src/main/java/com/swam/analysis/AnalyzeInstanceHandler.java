package com.swam.analysis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qesm.workflow.ProductInstance;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceTO;

@Service
public class AnalyzeInstanceHandler extends AbstractAnalyzeHandler<ProductInstance> {
    public AnalyzeInstanceHandler() {
        super(List.of(TargetMessageHandler.ANALYZE_INSTANCE), WorkflowInstanceTO.class, ProductInstance.class);
    }
}
