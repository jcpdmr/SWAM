package com.swam.commons.mongodb;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.qesm.AbstractProduct;

public interface WorkflowDTORepository<WFDTO extends AbstractWorkflowDTO<? extends AbstractProduct>, P extends AbstractProductDTO<? extends AbstractProduct>>
        extends MongoRepository<WFDTO, String> {

    @Aggregation(pipeline = {
            "{ $match: { '_id': ?0 } }",
            "{ $unwind: '$subWorkflowDTOList' }",
            "{ $match: { 'subWorkflowDTOList._id': ?1 } }",
            "{ $replaceRoot: { newRoot: '$subWorkflowDTOList' } }"
    })
    Optional<AbstractWorkflowDTO<? extends AbstractProduct>> findSpecificSubWorkflow(String workflowId,
            String subWorkflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $project: { vertex: { $getField: { field: ?1, input: '$vertexMap' } } } }",
            "{ $replaceRoot: { newRoot: '$vertex' } }"
    })
    Optional<AbstractProductDTO<? extends AbstractProduct>> findVertexByWorkflowIdAndVertexName(String workflowId,
            String vertexName);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $project: { vertexMap: { $objectToArray: '$vertexMap' } } }",
            "{ $unwind: '$vertexMap' }",
            "{ $replaceRoot: { newRoot: '$vertexMap.v' } }"
    })
    List<AbstractProductDTO<? extends AbstractProduct>> findAllVertexByWorkflowId(String workflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $project: { vertexMap: 1 , 'edgeSet' : null, _class : 1 } }",

    })
    Optional<AbstractWorkflowDTO<? extends AbstractProduct>> findVertexMapProjectionByWorkflowId(String workflowId);
}