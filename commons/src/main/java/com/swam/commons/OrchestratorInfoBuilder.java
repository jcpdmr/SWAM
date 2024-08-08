package com.swam.commons;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.util.Pair;

import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

public class OrchestratorInfoBuilder {

    private List<Pair<TargetMicroservices, TargetMethods>> pipeline = new ArrayList<>();

    private OrchestratorInfoBuilder(TargetMicroservices targetMicroservice, TargetMethods targetMethod) {
        Pair<TargetMicroservices, TargetMethods> targets = Pair.of(targetMicroservice, targetMethod);
        this.pipeline.add(targets);
    }

    public static OrchestratorInfoBuilder withTargets(TargetMicroservices targetMicroservice,
            TargetMethods targetMethod) {
        return new OrchestratorInfoBuilder(targetMicroservice, targetMethod);
    }

    public OrchestratorInfo build() {
        return new OrchestratorInfo(this.pipeline);
    }
}