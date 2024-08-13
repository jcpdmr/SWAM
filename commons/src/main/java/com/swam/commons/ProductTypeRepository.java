package com.swam.commons;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qesm.ProductType;

public interface ProductTypeRepository extends MongoRepository<ProductType, String> {

}
