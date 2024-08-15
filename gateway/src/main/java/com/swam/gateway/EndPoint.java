package com.swam.gateway;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpMethod;

import com.swam.commons.OrchestratorInfo.TargetMethods;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class EndPoint {

    private Map<TargetType, Map<HttpMethod, MethodInfo>> endPointData;

    // To specify request target
    enum TargetType {
        CATALOG,
        OPERATION,
        TO_BE_ISTANTIATED
    }

    // To define if a patternId is needed/optional/forbidden for a particular method
    enum Requirement {
        NEEDED,
        OPTIONAL,
        FORBIDDEN
    }

    // wrapper Object to contains ids and route orchestration about an http method
    @Setter
    @Getter
    @ToString
    protected static class MethodInfo {
        private List<Pair<TargetMicroservices, TargetMethods>> routingMap;
        private final Map<Requirement, List<String>> idsRequirementsMap;

        public MethodInfo() {
            this.routingMap = new ArrayList<>();
            this.idsRequirementsMap = new HashMap<>();
        }

        public void addIdRequirement(Requirement requirement, String id) {
            if (!idsRequirementsMap.containsKey(requirement)) {
                idsRequirementsMap.put(requirement, new ArrayList<>(List.of(id)));
            } else {
                idsRequirementsMap.get(requirement).add(id);
            }
        }
    }

    // Defining stepInterfaces to force using EndPointBuilder in a specific order
    public static EndPointBuilderStep1 builder() {
        return new EndPointBuilder();
    }

    public interface EndPointBuilderStep1 {
        EndPointBuilderStep2 withTargetType(TargetType targetType);
    }

    public interface EndPointBuilderStep2 {
        EndPointBuilderStep3 withMethod(HttpMethod httpMethod);
    }

    public interface EndPointBuilderStep3 {
        EndPointBuilderStep3 withNeededIds(String... neededIds);

        EndPointBuilderStep3 withOptionalIds(String... optionalIds);

        EndPointBuilderStep3 withForbiddenIds(String... forbiddenIds);

        EndPointBuilderStep4 withRouting(List<Pair<TargetMicroservices, TargetMethods>> routingEntries);

    }

    public interface EndPointBuilderStep4 {
        EndPointBuilderStep2 addTargetType(TargetType targetType);

        EndPointBuilderStep3 addMethod(HttpMethod httpMethod);

        EndPoint build();
    }

    public static class EndPointBuilder
            implements EndPointBuilderStep1, EndPointBuilderStep2, EndPointBuilderStep3, EndPointBuilderStep4 {

        private Map<TargetType, Map<HttpMethod, MethodInfo>> endPointData;
        private TargetType currentTargetType;
        private HttpMethod currenHttpMethod;

        @Override
        public EndPointBuilderStep2 withTargetType(TargetType targetType) {
            endPointData = new HashMap<>(Map.of(targetType, new HashMap<>()));
            currentTargetType = targetType;
            return this;
        }

        @Override
        public EndPointBuilderStep3 withMethod(HttpMethod httpMethod) {
            endPointData.get(currentTargetType).put(httpMethod, new MethodInfo());
            currenHttpMethod = httpMethod;
            return this;
        }

        @Override
        public EndPointBuilderStep3 withNeededIds(String... neededIds) {
            for (String string : neededIds) {
                endPointData.get(currentTargetType).get(currenHttpMethod).addIdRequirement(Requirement.NEEDED, string);
            }
            return this;
        }

        @Override
        public EndPointBuilderStep3 withOptionalIds(String... optionalIds) {
            for (String string : optionalIds) {
                endPointData.get(currentTargetType).get(currenHttpMethod).addIdRequirement(Requirement.OPTIONAL,
                        string);
            }
            return this;
        }

        @Override
        public EndPointBuilderStep3 withForbiddenIds(String... forbiddenIds) {
            for (String string : forbiddenIds) {
                endPointData.get(currentTargetType).get(currenHttpMethod).addIdRequirement(Requirement.FORBIDDEN,
                        string);
            }
            return this;
        }

        @Override
        public EndPointBuilderStep4 withRouting(List<Pair<TargetMicroservices, TargetMethods>> routingEntries) {
            endPointData.get(currentTargetType).get(currenHttpMethod).setRoutingMap(routingEntries);
            return this;
        }

        @Override
        public EndPointBuilderStep2 addTargetType(TargetType targetType) {
            endPointData.put(targetType, new HashMap<>());
            currentTargetType = targetType;
            return this;
        }

        @Override
        public EndPointBuilderStep3 addMethod(HttpMethod httpMethod) {
            endPointData.get(currentTargetType).put(httpMethod, new MethodInfo());
            currenHttpMethod = httpMethod;
            return this;
        }

        @Override
        public EndPoint build() {
            return new EndPoint(endPointData);
        }

    }

}