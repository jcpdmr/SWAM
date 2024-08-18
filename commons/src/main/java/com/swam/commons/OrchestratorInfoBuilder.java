package com.swam.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.swam.commons.OrchestratorInfo.TargetTasks;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

public class OrchestratorInfoBuilder {

    private List<Pair<TargetMicroservices, TargetTasks>> pipeline;
    private UUID uuid;

    private OrchestratorInfoBuilder() {
        this.pipeline = new ArrayList<>();
        this.uuid = null;
    }

    public static OrchestratorInfoBuilder newBuild() {
        return new OrchestratorInfoBuilder();
    }

    public OrchestratorInfoBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public OrchestratorInfoBuilder setTargets(List<Pair<TargetMicroservices, TargetTasks>> targets) {
        this.pipeline = targets;
        return this;
    }

    public OrchestratorInfoBuilder addTargets(TargetMicroservices targetMicroservice,
            TargetTasks targetMethod) {
        this.pipeline.add(Pair.of(targetMicroservice, targetMethod));
        return this;
    }

    public OrchestratorInfo build() {
        if (uuid != null) {
            return new OrchestratorInfo(this.pipeline, uuid);
        } else {
            return new OrchestratorInfo(this.pipeline);
        }
    }

}