package com.swam.commons.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qesm.AbstractProduct;

public interface WorkflowDTORepository<WFDTO extends AbstractWorkflowDTO<? extends AbstractProduct>>
                extends MongoRepository<WFDTO, String> {

}