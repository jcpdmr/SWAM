package com.swam.commons.intercommunication;

import java.util.ArrayList;
import java.util.List;

import com.swam.commons.intercommunication.RoutingInstructions.TargetMessageHandler;
import com.swam.commons.intercommunication.RoutingInstructions.TargetMicroservices;

public class RoutingInstructionsBuilder {

    private List<Pair<TargetMicroservices, TargetMessageHandler>> hopSequence;

    private RoutingInstructionsBuilder() {
        this.hopSequence = new ArrayList<>();
    }

    public static RoutingInstructionsBuilder newBuild() {
        return new RoutingInstructionsBuilder();
    }

    public RoutingInstructionsBuilder setTargets(List<Pair<TargetMicroservices, TargetMessageHandler>> targets) {
        this.hopSequence = targets;
        return this;
    }

    public RoutingInstructionsBuilder addTargets(TargetMicroservices targetMicroservice,
            TargetMessageHandler targetMethod) {
        this.hopSequence.add(Pair.of(targetMicroservice, targetMethod));
        return this;
    }

    public RoutingInstructions build() {

        return new RoutingInstructions(this.hopSequence);

    }

}