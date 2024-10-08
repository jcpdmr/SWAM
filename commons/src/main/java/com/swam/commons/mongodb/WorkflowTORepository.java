package com.swam.commons.mongodb;

import java.util.Optional;

import org.oristool.eulero.modeling.stochastictime.StochasticTime;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.qesm.workflow.AbstractProduct;

public interface WorkflowTORepository<WFTO extends AbstractWorkflowTO<? extends AbstractProduct>, P extends AbstractProductTO<? extends AbstractProduct>>
        extends MongoRepository<WFTO, String> {

    // @Aggregation(pipeline = {
    // "{ $match: { '_id': ?0 } }",
    // "{ $unwind: '$subWorkflowTOList' }",
    // "{ $match: { 'subWorkflowTOList._id': ?1 } }",
    // "{ $replaceRoot: { newRoot: '$subWorkflowTOList' } }"
    // })
    // Optional<AbstractWorkflowTO<? extends AbstractProduct>>
    // findSpecificSubWorkflow(String workflowId,
    // String subWorkflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 , 'vertexMap.?1': { $exists: true } } }",
            "{ $project: { vertex: { $getField: { field: ?1, input: '$vertexMap' } } } }",
            "{ $replaceRoot: { newRoot: '$vertex' } }"
    })
    Optional<AbstractProductTO<? extends AbstractProduct>> findVertexByWorkflowIdAndVertexName(String workflowId,
            String vertexName);

    // @Aggregation(pipeline = {
    // "{ $match: { _id: ?0 } }",
    // "{ $project: { vertexMap: { $objectToArray: '$vertexMap' } } }",
    // "{ $unwind: '$vertexMap' }",
    // "{ $replaceRoot: { newRoot: '$vertexMap.v' } }"
    // })
    // List<AbstractProductTO<? extends AbstractProduct>>
    // findAllVertexByWorkflowId(String workflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }",
            "{ $project: { vertexMap: 1 , 'edgeSet' : null, _class : 1 } }"
    })
    Optional<AbstractWorkflowTO<? extends AbstractProduct>> findVertexMapProjectionByWorkflowId(String workflowId);

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0, 'vertexMap.?1': { $exists: true } } }"
    })
    Optional<WFTO> findWorkflowIfVertexExists(String workflowId,
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