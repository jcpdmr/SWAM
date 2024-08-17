package com.swam.gateway;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class Controller {

    private final Dispatcher dispatcher;

    public Controller(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @RequestMapping("/**")
    public ResponseEntity<Object> handleRequest(@RequestParam(required = false) Map<String, String> requestParams,
            @RequestBody(required = false) String requestBody, HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        HttpMethod httpMethod;
        if (method.equalsIgnoreCase("GET")) {
            httpMethod = HttpMethod.GET;
        } else if (method.equalsIgnoreCase("POST")) {
            httpMethod = HttpMethod.POST;
        } else if (method.equalsIgnoreCase("PUT")) {
            httpMethod = HttpMethod.PUT;
        } else if (method.equalsIgnoreCase("DELETE")) {
            httpMethod = HttpMethod.DELETE;
        } else {
            return new ResponseEntity<>("Method:" + method + " not allowed", HttpStatusCode.valueOf(405));
        }

        return dispatcher.dispatchRequest(httpMethod, path, Optional.ofNullable(requestParams),
                Optional.ofNullable(requestBody));
    }
}
