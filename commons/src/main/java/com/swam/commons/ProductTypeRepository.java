package com.swam.commons;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductTypeRepository extends MongoRepository<ProductTypeDTO, String> {

}