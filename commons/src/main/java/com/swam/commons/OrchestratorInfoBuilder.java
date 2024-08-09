package com.swam.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.util.Pair;

import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

public class OrchestratorInfoBuilder {

    private List<Pair<TargetMicroservices, TargetMethods>> pipeline = new ArrayList<>();
    private UUID uuid;

    private OrchestratorInfoBuilder() {
        this.uuid = null;
    }

    public static OrchestratorInfoBuilder newBuild() {
        return new OrchestratorInfoBuilder();
    }

    public OrchestratorInfoBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public OrchestratorInfoBuilder addTargets(TargetMicroservices targetMicroservice,
            TargetMethods targetMethod) {
        this.pipeline.add(Pair.of(targetMicroservice, targetMethod));
        return this;
    }

    public OrchestratorInfo build() {
        if (uuid != null) {
            return new OrchestratorInfo(pipeline, uuid);
        } else {
            return new OrchestratorInfo(this.pipeline);
        }
    }

}