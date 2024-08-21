package com.swam.analysis;

import com.swam.commons.MessageDispatcher.MessageHandler;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;
import com.swam.commons.RoutingInstructions.TargetMessageHandler;

@Service
public class Analyze implements MessageHandler {

    @Override
    public void handle(CustomMessage context, TargetMessageHandler triggeredBinding) {
        // TODO: implement method
        System.out.println("Execute Analyze");
        if (context.getRequestBody().isPresent()) {
            System.out.println("RequestBody: " + context.getRequestBody().get());
        }
        if (context.getRequestParams().isPresent()) {
            System.out.println("RequestParam: " + context.getRequestParams().get());
        }
    }

    @Override
    public List<TargetMessageHandler> getBinding() {
        return List.of(TargetMessageHandler.ANALYZE);
    }

}
