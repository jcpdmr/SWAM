package com.swam.commons;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    /**
     * UUID associated with the [client] request (it's the same that directly
     * matches to a single deferredResult)
     */
    private final UUID requestUuid;
    /**
     * Field used in testing phase, just to pass simple example messages
     */
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
            MessageType messageType, ResponseEntity<String> responseEntity, UUID requestUuid) {
        this.msg = msg;
        this.orchestratorInfo = orchestratorInfo;
        this.sender = sender;
        this.messageType = messageType;
        this.responseStatusCode = responseEntity.getStatusCode().value();
        this.responseBody = responseEntity.getBody();
        this.requestUuid = requestUuid;
    }

    @JsonCreator
    public CustomMessage(String msg, OrchestratorInfo orchestratorInfo, TargetMicroservices sender,
            MessageType messageType, UUID uuid) {
        this.msg = msg;
        this.orchestratorInfo = orchestratorInfo;
        this.sender = sender;
        this.messageType = messageType;
        this.requestUuid = uuid;
    }

    /**
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> getResponseEntity() {
        return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(responseStatusCode));
    }
}
