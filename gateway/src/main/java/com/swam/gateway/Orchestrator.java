package com.swam.gateway;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.swam.commons.OrchestratorInfo;
import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;
import com.swam.commons.OrchestratorInfoBuilder;

@Service
public class Orchestrator {

    Map<UUID, OrchestratorInfo> activeOrchestration;

    public Orchestrator() {
        this.activeOrchestration = new HashMap<>();
    }

    public OrchestratorInfo computeOrchestratorInfo() {

        OrchestratorInfo orchestratorInfo = OrchestratorInfoBuilder.newBuild()
                .addTargets(TargetMicroservices.CATALOG, TargetMethods.ISTANCE_TEMPLATE)
                .addTargets(TargetMicroservices.OPERATION, TargetMethods.ANALYZE)
                .addTargets(TargetMicroservices.ANALYSIS, TargetMethods.NULL)
                .addTargets(TargetMicroservices.GATEWAY, TargetMethods.CHECK_ACK)
                .build();

        activeOrchestration.put(orchestratorInfo.getUuid(), orchestratorInfo);

        return orchestratorInfo;
    }

    public Optional<OrchestratorInfo> getActiveOrchestratorInfo(UUID orchestrationUUID) {
        if (activeOrchestration.containsKey(orchestrationUUID)) {
            return Optional.of(activeOrchestration.get(orchestrationUUID));
        } else {
            return Optional.empty();
        }
    }

    public Boolean removeActiveOrchestration(UUID orchestrationUUID) {
        if (activeOrchestration.containsKey(orchestrationUUID)) {
            activeOrchestration.remove(orchestrationUUID);
            return true;
        } else {
            return false;
        }
    }
}
