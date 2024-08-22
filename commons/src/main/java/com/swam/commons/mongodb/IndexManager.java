package com.swam.commons.mongodb;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class IndexManager {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void createIndexes() {
        // subworkflow index creation
        mongoTemplate.indexOps("Workflow")
                .ensureIndex(new CompoundIndexDefinition(
                        new Document("_id", 1)
                                .append("subWorkflowDTOList._id", 1)));
    }
}