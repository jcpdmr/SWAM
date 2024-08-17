package com.swam.commons;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

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
    private Integer responseStatusCode;
    private Object responseBody;
    private Map<String, String> uriTemplateVariables;
    private Optional<String> requestBody;
    private Optional<Map<String, String>> requestParams;
    private Optional<Integer> ackHop;

    public enum MessageType {
        ACK,
        TO_BE_FORWARDED,
        END_MESSAGE
    }

    public CustomMessage(String msg, OrchestratorInfo orchestratorInfo, TargetMicroservices sender,
            MessageType messageType, ResponseEntity<String> responseEntity) {
        this.msg = msg;
        this.orchestratorInfo = orchestratorInfo;
        this.sender = sender;
        this.messageType = messageType;
        this.responseStatusCode = responseEntity.getStatusCode().value();
        this.responseBody = responseEntity.getBody();
    }

    @JsonCreator
    public CustomMessage(String msg, OrchestratorInfo orchestratorInfo, TargetMicroservices sender,
            MessageType messageType) {
        this.msg = msg;
        this.orchestratorInfo = orchestratorInfo;
        this.sender = sender;
        this.messageType = messageType;
    }

    public ResponseEntity<Object> getResponseEntity() {
        return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(responseStatusCode));
    }
}
