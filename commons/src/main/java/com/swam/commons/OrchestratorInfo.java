package com.swam.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.data.util.Pair;

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
        NULL
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

    public OrchestratorInfo(MessageProperties messageProperties) {

        this.uuid = UUID.fromString(messageProperties.getHeader("orchestratorID"));
        this.hopCounter = Integer.valueOf(messageProperties.getHeader("hopCounter"));
        String targetMicroservices = messageProperties.getHeader("targetMicroservices");
        String targetMethods = messageProperties.getHeader("targetMethods");

        String[] targetMicroservicesArray = targetMicroservices.split(";");
        String[] targetMethodsArray = targetMethods.split(";");

        this.pipeline = new ArrayList<>();
        for (Integer index = 0; index < targetMicroservicesArray.length; index++) {
            pipeline.add(Pair.of(castToMicroservice(targetMicroservicesArray[index]),
                    castToMethod(targetMethodsArray[index])));
        }

    }

    private TargetMethods castToMethod(String methodToCast) {

        for (TargetMethods method : TargetMethods.values()) {
            if (methodToCast.equals(method.toString())) {
                return method;
            }
        }
        return null;
    }

    private TargetMicroservices castToMicroservice(String microserviceToCast) {

        for (TargetMicroservices microservice : TargetMicroservices.values()) {
            if (microserviceToCast.equals(microservice.toString())) {
                return microservice;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "OrchestratorInfo [hopCounter=" + hopCounter + ", pipeline=" + pipeline + "]";
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

    public Integer getHopCounter() {
        return hopCounter;
    }

    public Integer increaseHop() {
        hopCounter += 1;
        return hopCounter;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addHeadersToMessageProperties(MessageProperties messageProperties) {
        Map<String, Object> headers = messageProperties.getHeaders();
        headers.put("orchestratorID", uuid);
        headers.put("hopCounter", hopCounter.toString());
        String targetMicroservices = "";
        String targetMethods = "";
        for (Pair<TargetMicroservices, TargetMethods> pair : pipeline) {
            targetMicroservices += pair.getFirst().toString() + ";";
            targetMethods += pair.getSecond().toString() + ";";
        }
        headers.put("targetMicroservices", targetMicroservices);
        headers.put("targetMethods", targetMethods);
    }
}
