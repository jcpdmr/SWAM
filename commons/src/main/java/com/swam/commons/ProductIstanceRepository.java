package com.swam.commons;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductIstanceRepository extends MongoRepository<ProductIstanceDTO, String> {

}