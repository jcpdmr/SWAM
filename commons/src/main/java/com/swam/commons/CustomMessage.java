package com.swam.commons;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomMessage {
    private String msg;
    private Optional<Map<String, Object>> requestBody;
    private Optional<Map<String, String>> requestParam;
    private Optional<Integer> ackHop;

    @JsonCreator
    public CustomMessage(String msg) {
        this.msg = msg;
    }
}
