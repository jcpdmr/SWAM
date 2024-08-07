package com.swam.catalog;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qesm.ProductType;
import com.swam.commons.ProductDTO;

public interface ProductTypeRepository extends MongoRepository<ProductDTO<ProductType>, String> {

}
