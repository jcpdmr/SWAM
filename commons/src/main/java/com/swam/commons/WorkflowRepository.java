package com.swam.commons;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowRepository<T> extends MongoRepository<T, String> {

}