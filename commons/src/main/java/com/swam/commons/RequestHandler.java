package com.swam.commons;

import java.util.HashMap;
import java.util.Map;

import com.swam.commons.OrchestratorInfo.TargetMethods;

public class RequestHandler {

    private Map<TargetMethods, MethodExecutor> methodsMap;

    public RequestHandler() {
        this.methodsMap = new HashMap<>();
        methodsMap.put(TargetMethods.NULL, null);
    }

    public void addMethod(TargetMethods targetMethod, MethodExecutor methodExecutor) {
        methodsMap.put(targetMethod, methodExecutor);
    }

    public void handle(TargetMethods method) {
        // System.out.println("Lista di bindings dei metodi: " + methodsMap);
        // System.out.println("Nome del motodo da eseguire: " + method);
        if (method.equals(TargetMethods.NULL)) {
            return;
        }

        MethodExecutor methodExecutor = methodsMap.get(method);
        if (methodExecutor != null) {
            methodExecutor.execute();
        } else {
            System.out.println("Method: [" + method + "] not binded, current bindings: " + methodsMap);
        }

    }

    @FunctionalInterface
    public interface MethodExecutor {
        public void execute();
    }
}
