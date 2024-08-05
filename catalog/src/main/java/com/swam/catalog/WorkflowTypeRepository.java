package com.swam.catalog;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowTypeRepository extends MongoRepository<WorkflowTypeDTO, String> {

}
