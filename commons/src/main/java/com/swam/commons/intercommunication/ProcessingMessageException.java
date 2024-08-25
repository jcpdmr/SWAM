package com.swam.commons.intercommunication;

import lombok.Getter;

@Getter
public class ProcessingMessageException extends Exception {
    private String responseError;
    private Integer httpStatusCode;

    public ProcessingMessageException(String message, String responseError, Integer httpStatusCode) {
        super(message);
        this.responseError = responseError;
        this.httpStatusCode = httpStatusCode;
    }

    public ProcessingMessageException(String responseError, Integer httpStatusCode) {
        super(responseError);
        this.responseError = responseError;
        this.httpStatusCode = httpStatusCode;
    }

}
