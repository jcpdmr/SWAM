package com.swam.commons.mongodb;

import java.util.Optional;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.qesm.AbstractProduct;

public interface WorkflowDTORepository<WFDTO extends AbstractWorkflowDTO<? extends AbstractProduct>, P extends AbstractProductDTO<? extends AbstractProduct>>
        extends MongoRepository<WFDTO, String> {

    // @Aggregation(pipeline = {
    // "{ $match: { '_id': ?0 } }",
    // "{ $unwind: '$subWorkflowDTOList' }",
    // "{ $match: { 'subWorkflowDTOList._id': ?1 } }",
    // "{ $replaceRoot: { newRoot: '$subWorkflowDTOList' } }"
    // })
    // Optional<AbstractWorkflowDTO<? extends AbstractProduct>>
    // findSpecificSubWorkflow(String workflowId,
    // String subWorkflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $project: { vertex: { $getField: { field: ?1, input: '$vertexMap' } } } }",
            "{ $replaceRoot: { newRoot: '$vertex' } }"
    })
    Optional<AbstractProductDTO<? extends AbstractProduct>> findVertexByWorkflowIdAndVertexName(String workflowId,
            String vertexName);

    // @Aggregation(pipeline = {
    // "{ $match: { _id: ?0 } }",
    // "{ $project: { vertexMap: { $objectToArray: '$vertexMap' } } }",
    // "{ $unwind: '$vertexMap' }",
    // "{ $replaceRoot: { newRoot: '$vertexMap.v' } }"
    // })
    // List<AbstractProductDTO<? extends AbstractProduct>>
    // findAllVertexByWorkflowId(String workflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $project: { vertexMap: 1 , 'edgeSet' : null, _class : 1 } }"
    })
    Optional<AbstractWorkflowDTO<? extends AbstractProduct>> findVertexMapProjectionByWorkflowId(String workflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0, 'vertexMap.?1': { $exists: true } } }"
    })
    Optional<WFDTO> findWorkflowIfVertexExists(String workflowId,
            String productId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0} }",
            "{ $addFields: { isProcessed: { $eq: [ '$vertexMap.?1.itemGroup', 'PROCESSED' ] } } }",
            "{ $project: { _id: 0, isProcessed: 1 } }"
    })
    Boolean existVertexAndIsProcessed(String workflowId, String productId);

    @Query(value = "{ '_id': ?0 }")
    @Update(update = "{ $set: { 'vertexMap.?1.quantityProduced': ?2 , 'vertexMap.?1.pdf': ?3} }")
    Integer updateVertexQuantityProducedAndPdf(String workflowId, String vertexName, Integer quantityProduced,
            StochasticTime pdf);

    @Query(value = "{ '_id': ?0 }")
    @Update(update = "{ $set: { 'vertexMap.?1.quantityProduced': ?2} }")
    Integer updateVertexQuantityProduced(String workflowId, String vertexName, Integer quantityProduced);

    @Query(value = "{ '_id': ?0 }")
    @Update(update = "{ $set: { 'vertexMap.?1.pdf': ?2} }")
    Integer updateVertexPdf(String workflowId, String vertexName, StochasticTime pdf);
}