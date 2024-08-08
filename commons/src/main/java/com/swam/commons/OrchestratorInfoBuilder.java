package com.swam.commons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.util.Pair;

import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

public class OrchestratorInfoBuilder {

    private List<Pair<TargetMicroservices, TargetMethods>> pipeline = new ArrayList<>();

    private OrchestratorInfoBuilder() {

    }

    public static OrchestratorInfoBuilder newBuild() {
        return new OrchestratorInfoBuilder();
    }

    public OrchestratorInfoBuilder addTargets(TargetMicroservices targetMicroservice,
            TargetMethods targetMethod) {
        this.pipeline.add(Pair.of(targetMicroservice, targetMethod));
        return this;
    }

    public OrchestratorInfo build() {
        return new OrchestratorInfo(this.pipeline);
    }
}