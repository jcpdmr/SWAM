package com.swam.commons.mongodb.type;

import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;

import com.qesm.ProductType;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowDTORepository;

public interface WorkflowTypeDTORepository extends WorkflowDTORepository<WorkflowTypeDTO> {

    @Query("{ '_id': ?0, 'subWorkflowDTOList.id': ?1 }")
    Optional<AbstractWorkflowDTO<ProductType>> findByIdAndSubWorkflowDTOListId(String workflowId,
            String subWorkflowId);
}