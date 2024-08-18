package com.swam.catalog;

import com.swam.commons.MessageHandler.TaskExecutor;
import com.swam.commons.OrchestratorInfo.TargetTasks;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;

@Service
public class MakeIstance implements TaskExecutor {

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
    public TargetTasks getBinding() {
        return TargetTasks.ISTANCE_TEMPLATE;
    }

}
