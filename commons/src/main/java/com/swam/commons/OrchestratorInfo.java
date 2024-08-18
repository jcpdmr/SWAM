package com.swam.commons;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrchestratorInfo {

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

    private final UUID uuid;
    private Integer hopCounter;
    private List<Pair<TargetMicroservices, TargetTasks>> pipeline;

    public OrchestratorInfo(List<Pair<TargetMicroservices, TargetTasks>> pipeline) {
        this.uuid = UUID.randomUUID();
        this.hopCounter = 0;
        this.pipeline = pipeline;
    }

    public OrchestratorInfo(List<Pair<TargetMicroservices, TargetTasks>> pipeline, UUID uuid) {
        this.uuid = uuid;
        this.hopCounter = 0;
        this.pipeline = pipeline;
    }

    // @Override
    // public String toString() {
    // return "OrchestratorInfo [uuid=" + uuid + ", hopCounter=" + hopCounter + ",
    // pipeline=" + pipeline + "]";
    // }

    public TargetMicroservices getTargetMicroservice() {
        if (hopCounter.equals(pipeline.size())) {
            return TargetMicroservices.END;
        } else {
            return pipeline.get(hopCounter).getKey();
        }
    }

    public TargetTasks getTargetMethod() {
        return pipeline.get(hopCounter).getValue();
    }

    public Integer increaseHop() {
        hopCounter += 1;
        return hopCounter;
    }

    public Integer getMaxHop() {
        return pipeline.size();
    }
}
