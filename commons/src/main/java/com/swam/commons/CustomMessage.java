package com.swam.commons;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.swam.commons.RoutingInstructions.TargetMicroservices;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomMessage {
    /**
     * Field used in testing phase, just to pass simple example messages
     */
    private String msg;
    private String deferredResultId;
    private RoutingInstructions routingInstructions;
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
        END_MESSAGE,
        ERROR
    }

    public CustomMessage(String msg, RoutingInstructions routingInstructions, TargetMicroservices sender,
            MessageType messageType, ResponseEntity<Object> responseEntity) {
        this.msg = msg;
        this.routingInstructions = routingInstructions;
        this.sender = sender;
        this.messageType = messageType;
        this.responseStatusCode = responseEntity.getStatusCode().value();
        this.responseBody = responseEntity.getBody();
    }

    @JsonCreator
    public CustomMessage(String msg, RoutingInstructions routingInstructions, TargetMicroservices sender,
            MessageType messageType) {
        this.msg = msg;
        this.routingInstructions = routingInstructions;
        this.sender = sender;
        this.messageType = messageType;
    }

    /**
     * @return ResponseEntity<Object>
     */
    public ResponseEntity<Object> getResponseEntity() {
        return new ResponseEntity<>(responseBody, HttpStatusCode.valueOf(responseStatusCode));
    }

    public void setError(Object responseBody, Integer responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
        this.responseBody = responseBody;
        this.messageType = MessageType.ERROR;
    }
}
