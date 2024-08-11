package com.swam.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swam.commons.OrchestratorInfo;
import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;
import com.swam.commons.OrchestratorInfoBuilder;

@Configuration
public class OrchestratorConfig {

    @FunctionalInterface
    public interface OrchestrationPlanner {
        public default String getBindingPath() {
            return null;
        }

        public OrchestratorInfo orchestrate();
    }

    @Bean
    public OrchestrationPlanner orchestration1() {
        return new OrchestrationPlanner() {

            @Override
            public String getBindingPath() {
                return "workflow";
            }

            @Override
            public OrchestratorInfo orchestrate() {
                return OrchestratorInfoBuilder.newBuild()
                        .addTargets(TargetMicroservices.CATALOG, TargetMethods.ISTANCE_TEMPLATE)
                        .addTargets(TargetMicroservices.OPERATION, TargetMethods.MAKE_PERSISTENCE)
                        .addTargets(TargetMicroservices.ANALYSIS, TargetMethods.ANALYZE)
                        .addTargets(TargetMicroservices.GATEWAY, TargetMethods.CHECK_ACK)
                        .build();
            }

        };
    }

}
