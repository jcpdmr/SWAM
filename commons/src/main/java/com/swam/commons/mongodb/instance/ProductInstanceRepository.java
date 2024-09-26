package com.swam.commons.mongodb.instance;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductInstanceRepository extends MongoRepository<ProductInstanceTO, String> {

}