package com.swam.gateway;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
public class AsyncResponseHandler {

    private final Map<String, DeferredResult<ResponseEntity<Object>>> deferredResultMap = new HashMap<>();

    public Entry<String, DeferredResult<ResponseEntity<Object>>> newDeferredResult() {
        // TODO: chose timeout value
        DeferredResult<ResponseEntity<Object>> deferredResult = new DeferredResult<>(5000L);

        // Timeout action
        deferredResult.onTimeout(() -> deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body("Request timeout")));

        // Error action
        deferredResult.onError((Throwable throwable) -> deferredResult
                .setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Internal server error: " + throwable.getMessage())));

        UUID deferredResultUuid = UUID.randomUUID();

        deferredResultMap.put(deferredResultUuid.toString(), deferredResult);
        return Map.entry(deferredResultUuid.toString(), deferredResult);

    }

    public void setDeferredResult(String deferredResultId, ResponseEntity<Object> result) {
        // TODO: handle uuid not found
        deferredResultMap.remove(deferredResultId).setResult(result);
    }

}
