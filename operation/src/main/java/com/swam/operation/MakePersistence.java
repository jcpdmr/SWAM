package com.swam.operation;

import com.swam.commons.MessageHandler.MethodExecutor;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo.TargetMethods;

@Service
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

    @Override
    public TargetMethods getBinding() {
        return TargetMethods.MAKE_PERSISTENCE;
    }

}
