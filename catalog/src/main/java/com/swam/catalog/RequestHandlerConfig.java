package com.swam.catalog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swam.commons.RequestHandler;
import com.swam.commons.OrchestratorInfo.TargetMethods;

@Configuration
public class RequestHandlerConfig {

    @Bean(name = "catalog")
    public RequestHandler requestHandler() {

        RequestHandler requestHandler = new RequestHandler();
        requestHandler.addMethod(TargetMethods.ISTANCE_TEMPLATE, new MakeIstance());

        return requestHandler;
    }
}
