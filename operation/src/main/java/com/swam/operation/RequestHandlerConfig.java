package com.swam.operation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swam.commons.RequestHandler;
import com.swam.commons.OrchestratorInfo.TargetMethods;

@Configuration
public class RequestHandlerConfig {

    @Bean(name = "operation")
    public RequestHandler requestHandler() {

        RequestHandler requestHandler = new RequestHandler();
        requestHandler.addMethod(TargetMethods.ANALYZE, new Analyze());

        return requestHandler;
    }
}
