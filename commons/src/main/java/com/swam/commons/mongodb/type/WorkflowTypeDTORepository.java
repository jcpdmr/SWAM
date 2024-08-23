package com.swam.commons.mongodb.type;

import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;

import com.qesm.ProductType;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

public interface WorkflowTypeDTORepository extends WorkflowDTORepository<WorkflowTypeDTO> {

    @Aggregation(pipeline = {
            "{ $match: { '_id': ?0 } }",
            "{ $unwind: '$subWorkflowDTOList' }",
            "{ $match: { 'subWorkflowDTOList._id': ?1 } }",
            "{ $replaceRoot: { newRoot: '$subWorkflowDTOList' } }"
    })
    Optional<AbstractWorkflowDTO<ProductType>> findSpecificSubWorkflow(String workflowId, String subWorkflowId);
}