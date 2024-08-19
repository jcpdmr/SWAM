package com.swam.gateway;

import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class Controller {

    private final Dispatcher dispatcher;
    private final AsyncResponseHandler asyncResponseHandler;

    public Controller(Dispatcher dispatcher, AsyncResponseHandler asyncResponseHandler) {
        this.dispatcher = dispatcher;
        this.asyncResponseHandler = asyncResponseHandler;
    }

    @RequestMapping("/**")
    public DeferredResult<ResponseEntity<Object>> handleRequest(
            @RequestParam(required = false) Map<String, String> requestParams,
            @RequestBody(required = false) String requestBody, HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        HttpMethod httpMethod;
        Entry<String, DeferredResult<ResponseEntity<Object>>> deferredResultEntry = asyncResponseHandler
                .newDeferredResult();
        if (method.equalsIgnoreCase("GET")) {
            httpMethod = HttpMethod.GET;
        } else if (method.equalsIgnoreCase("POST")) {
            httpMethod = HttpMethod.POST;
        } else if (method.equalsIgnoreCase("PUT")) {
            httpMethod = HttpMethod.PUT;
        } else if (method.equalsIgnoreCase("DELETE")) {
            httpMethod = HttpMethod.DELETE;
        } else {
            asyncResponseHandler.setDeferredResult(deferredResultEntry.getKey(),
                    new ResponseEntity<Object>("Method:" + method + " not allowed", HttpStatusCode.valueOf(405)));
            return deferredResultEntry.getValue();
        }

        dispatcher.dispatchRequest(httpMethod, path, Optional.ofNullable(requestParams),
                Optional.ofNullable(requestBody), deferredResultEntry.getKey());

        return deferredResultEntry.getValue();
    }
}
