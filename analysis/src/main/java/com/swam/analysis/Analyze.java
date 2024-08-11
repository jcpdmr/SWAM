package com.swam.analysis;

import com.swam.commons.AbstractMessageHandler.MethodExecutor;
import com.swam.commons.CustomMessage;

public class Analyze implements MethodExecutor {

    @Override
    public void execute(CustomMessage context) {
        // TODO: implement method
        System.out.println("Execute Analyze");
        if (context.getRequestBody().isPresent()) {
            System.out.println("RequestBody: " + context.getRequestBody().get());
        }
        if (context.getParamMap().isPresent()) {
            System.out.println("RequestParam: " + context.getParamMap().get());
        }
    }

}
