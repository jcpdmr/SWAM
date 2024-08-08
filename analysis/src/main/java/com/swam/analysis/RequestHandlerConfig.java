package com.swam.analysis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swam.commons.RequestHandler;

@Configuration
public class RequestHandlerConfig {

    @Bean(name = "analysis")
    public RequestHandler requestHandler() {

        RequestHandler requestHandler = new RequestHandler();
        // TODO: add methods
        // requestHandler.addMethod(TargetMethods.ANALYZE, new Analyze());

        return requestHandler;
    }
}
