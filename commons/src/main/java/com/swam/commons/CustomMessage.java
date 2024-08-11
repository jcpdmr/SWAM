package com.swam.commons;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.swam.commons.OrchestratorInfo.TargetMicroservices;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomMessage {
    private String msg;
    private OrchestratorInfo orchestratorInfo;
    private TargetMicroservices sender;
    private MessageType messageType;
    private Optional<String> requestBody;
    private Optional<Map<String, List<String>>> paramMap;
    private Optional<Integer> ackHop;

    public enum MessageType {
        ACK,
        TO_BE_FORWARDED,
        END_MESSAGE
    }

    @JsonCreator
    public CustomMessage(String msg, OrchestratorInfo orchestratorInfo, TargetMicroservices sender,
            MessageType messageType) {
        this.msg = msg;
        this.orchestratorInfo = orchestratorInfo;
        this.sender = sender;
        this.messageType = messageType;
    }

}
