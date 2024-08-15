package com.swam.commons;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowTypeRepository extends MongoRepository<WorkflowTypeDTO, String> {

}