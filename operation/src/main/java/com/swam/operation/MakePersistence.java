package com.swam.operation;

import com.swam.commons.MessageDispatcher.TaskExecutor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swam.commons.CustomMessage;
import com.swam.commons.RoutingInstructions.TargetTasks;

@Service
public class MakePersistence implements TaskExecutor {

    @Override
    public void execute(CustomMessage context, TargetTasks triggeredBinding) {
        // TODO: implement method
        System.out.println("Execute MakePersistence");
        if (context.getRequestBody().isPresent()) {
            System.out.println("RequestBody: " + context.getRequestBody().get());
        }
        if (context.getRequestParams().isPresent()) {
            System.out.println("RequestParam: " + context.getRequestParams().get());
        }
    }

    @Override
    public List<TargetTasks> getBinding() {
        return List.of(TargetTasks.MAKE_PERSISTENCE);
    }

}
