package com.swam.commons.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface MongodbDTO {
    @JsonIgnore
    public Boolean isValid();
}
