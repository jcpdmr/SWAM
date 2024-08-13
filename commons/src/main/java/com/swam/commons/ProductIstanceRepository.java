package com.swam.commons;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qesm.ProductIstance;

public interface ProductIstanceRepository extends MongoRepository<ProductIstance, String> {

}