package com.swam.commons.mongodb.type;

import java.util.Optional;

import org.springframework.data.mongodb.repository.Query;

import com.qesm.ProductType;
import com.qesm.WorkflowType;
import com.swam.commons.mongodb.AbstractWorkflowDTO;
import com.swam.commons.mongodb.WorkflowRepository;

public interface WorkflowTypeRepository extends WorkflowRepository<WorkflowTypeDTO> {

    @Query("{ '_id': ?0, 'subWorkflowDTOList.id': ?1 }")
    Optional<AbstractWorkflowDTO<ProductType, WorkflowType>> findByIdAndSubWorkflowDTOListId(String workflowId,
            String subWorkflowId);

}