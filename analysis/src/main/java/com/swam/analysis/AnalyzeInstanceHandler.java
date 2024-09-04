package com.swam.analysis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qesm.ProductInstance;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.mongodb.instance.WorkflowInstanceDTO;

@Service
public class AnalyzeInstanceHandler extends AbstractAnalyzeHandler<ProductInstance> {
    public AnalyzeInstanceHandler() {
        super(List.of(TargetMessageHandler.ANALYZE_INSTANCE), WorkflowInstanceDTO.class, ProductInstance.class);
    }
}
