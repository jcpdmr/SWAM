package com.swam.catalog;

import com.swam.commons.MessageHandler.MethodExecutor;
import com.swam.commons.OrchestratorInfo.TargetMethods;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;

@Service
public class MakeIstance implements MethodExecutor {

    @Override
    public void execute(CustomMessage context) {
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
    public TargetMethods getBinding() {
        return TargetMethods.ISTANCE_TEMPLATE;
    }

}
