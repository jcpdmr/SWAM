package com.swam.analysis;

import com.swam.commons.MessageHandler.MethodExecutor;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo.TargetMethods;

@Service
public class Analyze implements MethodExecutor {

    @Override
    public void execute(CustomMessage context) {
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
    public TargetMethods getBinding() {
        return TargetMethods.ANALYZE;
    }

}
