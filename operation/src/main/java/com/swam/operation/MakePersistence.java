package com.swam.operation;

import com.swam.commons.AbstractMessageHandler.MethodExecutor;
import com.swam.commons.CustomMessage;

public class MakePersistence implements MethodExecutor {

    @Override
    public void execute(CustomMessage context) {
        // TODO: implement method
        System.out.println("Execute MakePersistence");
        if (context.getRequestBody().isPresent()) {
            System.out.println("RequestBody: " + context.getRequestBody().get());
        }
        if (context.getParamMap().isPresent()) {
            System.out.println("RequestParam: " + context.getParamMap().get());
        }
    }

}
