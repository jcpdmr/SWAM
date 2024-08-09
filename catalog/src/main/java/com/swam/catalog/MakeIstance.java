package com.swam.catalog;

import com.swam.commons.AbstractMessageHandler.MethodExecutor;
import com.swam.commons.CustomMessage;

public class MakeIstance implements MethodExecutor {

    @Override
    public void execute(CustomMessage context) {
        // TODO: implement method
        System.out.println("Execute makeIstance");
        if (context.getRequestBody().isPresent()) {
            System.out.println("RequestBody: " + context.getRequestBody().get());
        }
        if (context.getRequestParam().isPresent()) {
            System.out.println("RequestParam: " + context.getRequestParam().get());
        }
    }

}
