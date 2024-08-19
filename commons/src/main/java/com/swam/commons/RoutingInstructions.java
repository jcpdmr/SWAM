package com.swam.commons;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RoutingInstructions {

    public enum TargetTasks {

        ANALYZE,
        ISTANCE_TEMPLATE,
        CHECK_ACK,
        NULL,
        MAKE_PERSISTENCE,

        GET_WORKFLOW,
        GET_PRODUCT,
        GET_ANALYSIS,

        PUT_WORKFLOW,
        PUT_PRODUCT,
        PUT_ANALYSIS,

        POST_WORKFLOW,
        POST_PRODUCT,
        POST_ANALYSIS,

        DELETE_WORKFLOW,
        DELETE_PRODUCT,
        DELETE_ANALYSIS,
    }

    public enum TargetMicroservices {
        CATALOG,
        OPERATION,
        ANALYSIS,
        GATEWAY,
        END
    }

    private Integer hopCounter;
    private List<Pair<TargetMicroservices, TargetTasks>> hopSequence;

    @JsonCreator
    public RoutingInstructions(List<Pair<TargetMicroservices, TargetTasks>> hopSequence) {
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

    public TargetTasks getTargetMethod() {
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
