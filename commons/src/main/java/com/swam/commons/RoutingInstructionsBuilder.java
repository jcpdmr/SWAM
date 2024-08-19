package com.swam.commons;

import java.util.ArrayList;
import java.util.List;

import com.swam.commons.RoutingInstructions.TargetTasks;
import com.swam.commons.RoutingInstructions.TargetMicroservices;

public class RoutingInstructionsBuilder {

    private List<Pair<TargetMicroservices, TargetTasks>> hopSequence;

    private RoutingInstructionsBuilder() {
        this.hopSequence = new ArrayList<>();
    }

    public static RoutingInstructionsBuilder newBuild() {
        return new RoutingInstructionsBuilder();
    }

    public RoutingInstructionsBuilder setTargets(List<Pair<TargetMicroservices, TargetTasks>> targets) {
        this.hopSequence = targets;
        return this;
    }

    public RoutingInstructionsBuilder addTargets(TargetMicroservices targetMicroservice,
            TargetTasks targetMethod) {
        this.hopSequence.add(Pair.of(targetMicroservice, targetMethod));
        return this;
    }

    public RoutingInstructions build() {

        return new RoutingInstructions(this.hopSequence);

    }

}