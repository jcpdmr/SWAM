package com.swam.gateway;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    private final Dispatcher dispatcher;

    public Controller(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @GetMapping("/dev/{path}")
    public ResponseEntity<String> handleGetRequest(
            @PathVariable String path,
            @RequestParam(required = false) Map<String, String> queryParams) {

        return dispatcher.dispatchRequest(path, Optional.of(queryParams), Optional.empty());
    }

    @PostMapping("/dev/{path}")
    public ResponseEntity<String> handlePostRequest(
            @PathVariable String path,
            @RequestBody(required = false) String requestBody) {

        return dispatcher.dispatchRequest(path, Optional.empty(), Optional.of(requestBody));
    }
}
