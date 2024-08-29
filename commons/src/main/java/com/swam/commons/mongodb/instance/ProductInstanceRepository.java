package com.swam.commons.mongodb.instance;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductInstanceRepository extends MongoRepository<ProductInstanceDTO, String> {

}