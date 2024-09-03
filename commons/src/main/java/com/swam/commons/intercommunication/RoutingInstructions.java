package com.swam.commons.intercommunication;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RoutingInstructions {

    public enum TargetMessageHandler {

        ISTANCE_TEMPLATE,
        CHECK_ACK,
        NULL,
        MAKE_PERSISTENCE,
        FORWARD_WORKFLOW,

        WORKFLOW,
        PRODUCT,
        ANALYZE_TEMPLATE,
        ANALYZE_INSTANCE
    }

    public enum TargetMicroservices {
        CATALOG,
        OPERATION,
        ANALYSIS,
        GATEWAY,
        END
    }

    private Integer hopCounter;
    private List<Pair<TargetMicroservices, TargetMessageHandler>> hopSequence;

    @JsonCreator
    public RoutingInstructions(List<Pair<TargetMicroservices, TargetMessageHandler>> hopSequence) {
        this.hopCounter = 0;
        this.hopSequence = hopSequence;
    }

    public TargetMicroservices getTargetMicroservice() {
        if (hopCounter.equals(hopSequence.size())) {
            return TargetMicroservices.END;
        } else {
            return hopSequence.get(hopCounter).getKey();
        }
    }

    public TargetMessageHandler getTargetMethod() {
        return hopSequence.get(hopCounter).getValue();
    }

    public Integer increaseHop() {
        hopCounter += 1;
        return hopCounter;
    }

    public Integer getMaxHop() {
        return hopSequence.size();
    }
}
