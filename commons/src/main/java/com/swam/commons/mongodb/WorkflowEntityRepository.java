package com.swam.commons.mongodb;

import java.util.Optional;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.qesm.workflow.AbstractProduct;

public interface WorkflowEntityRepository<WFE extends AbstractWorkflowEntity<? extends AbstractProduct>, P extends AbstractProductEntity<? extends AbstractProduct>>
        extends MongoRepository<WFE, String> {

    // @Aggregation(pipeline = {
    // "{ $match: { '_id': ?0 } }",
    // "{ $unwind: '$subWorkflowEntityList' }",
    // "{ $match: { 'subWorkflowEntityList._id': ?1 } }",
    // "{ $replaceRoot: { newRoot: '$subWorkflowEntityList' } }"
    // })
    // Optional<AbstractWorkflowEntity<? extends AbstractProduct>>
    // findSpecificSubWorkflow(String workflowId,
    // String subWorkflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 , 'vertexMap.?1': { $exists: true } } }",
            "{ $project: { vertex: { $getField: { field: ?1, input: '$vertexMap' } } } }",
            "{ $replaceRoot: { newRoot: '$vertex' } }"
    })
    Optional<AbstractProductEntity<? extends AbstractProduct>> findVertexByWorkflowIdAndVertexName(String workflowId,
            String vertexName);

    // @Aggregation(pipeline = {
    // "{ $match: { _id: ?0 } }",
    // "{ $project: { vertexMap: { $objectToArray: '$vertexMap' } } }",
    // "{ $unwind: '$vertexMap' }",
    // "{ $replaceRoot: { newRoot: '$vertexMap.v' } }"
    // })
    // List<AbstractProductEntity<? extends AbstractProduct>>
    // findAllVertexByWorkflowId(String workflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $project: { vertexMap: 1 , 'edgeSet' : null, _class : 1 } }"
    })
    Optional<AbstractWorkflowEntity<? extends AbstractProduct>> findVertexMapProjectionByWorkflowId(String workflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0, 'vertexMap.?1': { $exists: true } } }"
    })
    Optional<WFE> findWorkflowIfVertexExists(String workflowId,
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