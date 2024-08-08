package com.swam.catalog;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qesm.ProductIstance;
import com.swam.commons.ProductDTO;

public interface ProductIstanceRepository extends MongoRepository<ProductDTO<ProductIstance>, String> {

}
