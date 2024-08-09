package com.swam.commons;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomMessage {
    private String msg;
    private Map<String, Object> requestBody;

    @JsonCreator
    public CustomMessage(String msg) {
        this.msg = msg;
    }
}
