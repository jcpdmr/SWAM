package com.swam.commons.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swam.commons.intercommunication.ProcessingMessageException;

public interface MongodbDTO<T> {
    @JsonIgnore
    public T convertAndValidate() throws ProcessingMessageException;

    @SuppressWarnings("unchecked")
    public default <C> C uncheckedCast(Object objectToCast) {
        return (C) objectToCast;
    }
}
