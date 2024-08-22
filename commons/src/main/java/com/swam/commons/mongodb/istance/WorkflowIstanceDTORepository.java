package com.swam.commons.mongodb.istance;

import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;

import com.qesm.ProductIstance;
import com.swam.commons.mongodb.AbstractBaseWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

public interface WorkflowIstanceDTORepository extends WorkflowDTORepository<HeadWorkflowIstanceDTO> {

    @Aggregation(pipeline = {
            "{ $match: { '_id': ?0 } }",
            "{ $unwind: '$subWorkflowDTOList' }",
            "{ $match: { 'subWorkflowDTOList._id': ?1 } }",
            "{ $replaceRoot: { newRoot: '$subWorkflowDTOList' } }"
    })
    Optional<AbstractBaseWorkflowDTO<ProductIstance>> findSpecificSubWorkflow(String workflowId, String subWorkflowId);
}