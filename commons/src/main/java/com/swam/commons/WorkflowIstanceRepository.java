package com.swam.commons;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowIstanceRepository extends MongoRepository<WorkflowIstanceDTO, String> {

}