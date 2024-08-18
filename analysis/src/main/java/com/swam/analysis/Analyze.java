package com.swam.analysis;

import com.swam.commons.MessageHandler.TaskExecutor;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;
import com.swam.commons.OrchestratorInfo.TargetTasks;

@Service
public class Analyze implements TaskExecutor {

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
    public TargetTasks getBinding() {
        return TargetTasks.ANALYZE;
    }

}
