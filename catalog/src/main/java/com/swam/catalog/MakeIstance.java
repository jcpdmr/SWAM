package com.swam.catalog;

import com.swam.commons.MessageDispatcher.MessageHandler;
import com.swam.commons.RoutingInstructions.TargetMessageHandler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;

@Service
public class MakeIstance implements MessageHandler {

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) {
        // TODO: implement method
        System.out.println("Execute MakeIstance");
        if (context.getRequestBody().isPresent()) {
            System.out.println("RequestBody: " + context.getRequestBody().get());
        }
        if (context.getRequestParams().isPresent()) {
            System.out.println("RequestParam: " + context.getRequestParams().get());
        }
    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.ISTANCE_TEMPLATE);
    }

}
