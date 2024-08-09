package com.swam.commons;

import java.util.List;
import java.util.UUID;

import org.springframework.data.util.Pair;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrchestratorInfo {

    public enum TargetMethods {

        // analysis's methods
        ANALYZE,
        // catalog's methods
        ISTANCE_TEMPLATE,
        // operation's methods
        // gateway's methods
        CHECK_ACK,
        // common's methods
        NULL,
        MAKE_PERSISTENCE
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
    private List<Pair<TargetMicroservices, TargetMethods>> pipeline;

    public OrchestratorInfo(List<Pair<TargetMicroservices, TargetMethods>> pipeline) {
        this.uuid = UUID.randomUUID();
        this.hopCounter = 0;
        this.pipeline = pipeline;
    }

    public OrchestratorInfo(List<Pair<TargetMicroservices, TargetMethods>> pipeline, UUID uuid) {
        this.uuid = uuid;
        this.hopCounter = 0;
        this.pipeline = pipeline;
    }

    @Override
    public String toString() {
        return "OrchestratorInfo [uuid=" + uuid + ", hopCounter=" + hopCounter + ", pipeline=" + pipeline + "]";
    }

    public TargetMicroservices getTargetMicroservice() {
        if (hopCounter.equals(pipeline.size())) {
            return TargetMicroservices.END;
        } else {
            return pipeline.get(hopCounter).getFirst();
        }
    }

    public TargetMethods getTargetMethod() {
        return pipeline.get(hopCounter).getSecond();
    }

    public Integer increaseHop() {
        hopCounter += 1;
        return hopCounter;
    }

    public Integer getMaxHop() {
        return pipeline.size();
    }
}
